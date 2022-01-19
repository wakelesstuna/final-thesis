package io.wakelesstuna.userdgs.services;

import com.netflix.graphql.dgs.DgsComponent;
import graphql.GraphQLException;
import graphql.relay.*;
import io.wakelesstuna.user.generated.types.*;
import io.wakelesstuna.userdgs.connection.CursorUtil;
import io.wakelesstuna.userdgs.persistence.*;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a service class that handle the logic for the {@link UserEntity}.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final ServiceHelper serviceHelper;
    private final CursorUtil cursorUtil;
    private final FollowRepository followRepo;
    private final UserRepository userRepo;

    /**
     * Fetching all User entities from the database an maps them
     * to a GraphQL typ of User
     *
     * @return List of users
     */
    public List<User> getUsers() {
        return userRepo.findAll().stream()
                .map(UserEntity::mapToUserType)
                .collect(Collectors.toList());
    }

    /**
     * Fetching a user form the database based on the user id
     *
     * @param id id of the user to fetch
     * @return User
     */
    public User getUser(UUID id) {
        return serviceHelper.getUser(id).mapToUserType();
    }

    /**
     * This method creates a user and persist it to the database.
     * It send a request to the image-service for uploading a file,
     * it gets a string in return with the downloading link.
     * Then it creates a user object and persist it to the database.
     *
     * @param input information about the user to create
     * @param file  file to upload
     * @return User the created user
     */
    public User createUser(CreateUserInput input, MultipartFile file) {
        log.info("In Create user! {}", file);
        UUID userId = UUID.randomUUID();
        serviceHelper.checkIfUsernameUnique(input.getUsername());
        serviceHelper.checkIfEmailUnique(input.getEmail());
        serviceHelper.checkIfPhoneUnique(input.getPhone());
        serviceHelper.validatePassword(input.getPassword());

        final String imageUrl = serviceHelper.uploadImage(userId, file);

        UserEntity newUser = UserEntity.builder()
                .id(userId)
                .username(input.getUsername())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .phone(input.getPhone())
                .email(input.getEmail())
                .password(input.getPassword())
                .profilePic(imageUrl)
                .createdAt(serviceHelper.getLocalDateTime())
                .build();

        userRepo.saveAndFlush(newUser);
        log.info("New user created {}", newUser.getUsername());
        return newUser.mapToUserType();
    }


    @Transactional
    public User updateUser(UpdateUserInput input, MultipartFile file) {
        var userToUpdate = serviceHelper.getUser(input.getUserId());
        if (input.getFirstName() != null) {
            userToUpdate.setFirstName(input.getFirstName());
            log.info("Updating user first name");
        }
        if (input.getLastName() != null) {
            userToUpdate.setLastName(input.getLastName());
            log.info("Updating user last name");
        }
        if (input.getEmail() != null && !input.getEmail().equals(userToUpdate.getEmail())) {
            serviceHelper.checkIfEmailUnique(input.getEmail());
            userToUpdate.setEmail(input.getEmail());
            log.info("Updating user email");
        }
        if (input.getUsername() != null && !input.getUsername().equals(userToUpdate.getUsername())) {
            serviceHelper.checkIfUsernameUnique(input.getUsername());
            userToUpdate.setUsername(input.getUsername());
            log.info("Updating username");
        }
        if (input.getPhone() != null && !input.getPhone().equals(userToUpdate.getPhone())) {
            serviceHelper.checkIfPhoneUnique(input.getPhone());
            userToUpdate.setPhone(input.getPhone());
            log.info("Updating phone number");
        }
        if (input.getDescription() != null) {
            serviceHelper.checkIfDescriptionIsValid(input.getDescription());
            userToUpdate.setDescription(input.getDescription());
            log.info("Updating description");
        }
        if (file != null) {
            serviceHelper.checkIfImageSize(file.getSize());
            final String imageUrl = serviceHelper.uploadImage(userToUpdate.getId(), file);
            userToUpdate.setProfilePic(imageUrl);
            log.info("Updating user profile pic");
        }

        userRepo.saveAndFlush(userToUpdate);
        log.info("Saving user updates: {}", userToUpdate.mapToUserType().toString());
        return userToUpdate.mapToUserType();
    }

    @Transactional
    public String deleteUser(AuthUserInput authUserInput) {
        // find user
        var userToDelete = serviceHelper.getUser(authUserInput.getUserId());
        // TODO: 2021-12-13 Kolla att lösenordet stämmer innan delete utför
        var userId = userToDelete.getId();


        // delete all follows
        followRepo.deleteAllByUserId(userId);
        log.info("Deleting all follows of user");
        // delete all followings
        followRepo.deleteAllByFollowId(userId);
        log.info("Deleting all followers of user");

        // delete user
        log.info("Deleting user");
        userRepo.delete(userToDelete);
        log.info("User deleted");

        return HttpStatus.ACCEPTED.toString();
    }

    public void sendDeleteRequest() {
        // TODO: 2022-01-13 send request to post service to delete all posts
        // delete all post
        // postRepo.deleteAllByUserId(userId);
        log.info("Deleting all posts of user");

        // TODO: 2022-01-13 send request to post service to delete all likes
        // delete all likes
        // likeRepo.deleteAllByUserId(userId);
        log.info("Deleting all likes of user");

        // TODO: 2022-01-13 send request to post service to delete all comments
        // delete all comments
        // commentRepo.deleteAllByUserId(userId);
        log.info("Deleting all comments of user");

    }


    public Connection<User> getPaginationUser(Integer first, @Nullable String cursor) {
        List<Edge<User>> edges = getUsersWithCursor(cursor).stream()
                .map(user -> new DefaultEdge<>(user, cursorUtil.createCursorWith(user.getId())))
                .limit(first)
                .collect(Collectors.toList());

        var pageInfo = new DefaultPageInfo(
                cursorUtil.getFirstCursorFrom(edges),
                cursorUtil.getLastCursorFrom(edges),
                cursor != null,
                edges.size() >= first);

        return new DefaultConnection<>(edges, pageInfo);
    }

    private List<User> getUsersWithCursor(String cursor) {
        if (cursor == null) return getUsers();

        return getUsersAfter(cursorUtil.decode(cursor));
    }

    private List<User> getUsersAfter(UUID id) {
        return userRepo.findAll().stream()
                .dropWhile(user -> user.getId().compareTo(id) != 1)
                .collect(Collectors.toList()).stream()
                .map(UserEntity::mapToUserType)
                .collect(Collectors.toList());
    }


    public User authenticateUser(AuthUserInput authUserInput) {
        var username = authUserInput.getUsername();
        var user = userRepo.findByUsernameOrEmailOrPhone(
                username,
                username,
                username)
                .orElseThrow(() -> new GraphQLException(HttpStatus.UNAUTHORIZED.toString()));

        if (user.getPassword().equals(authUserInput.getPassword())) {
            return user.mapToUserType();
        }
        throw new GraphQLException(HttpStatus.UNAUTHORIZED.toString());
    }

    public List<User> getRandomUsers(Integer howMany, String username) {
        return userRepo.findRandomButNotWith(username, howMany).stream()
                .map(UserEntity::mapToUserType)
                .collect(Collectors.toList());
    }

    public Boolean exitsByUsername(String username) {
        log.info("Checking if username exists: {}", username);
        return userRepo.existsByUsername(username);
    }

    public Boolean exitsByEmail(String email) {
        log.info("Checking if email exists: {}", email);
        return userRepo.existsByEmail(email);
    }

    public Boolean exitsByPhone(String phone) {
        log.info("Checking if email exists: {}", phone);
        return userRepo.existsByPhone(phone);
    }

    public Boolean updatePassword(UpdatePasswordInput input) {
        var user = serviceHelper.getUser(input.getUserId());
        if (user.getPassword().equals(input.getOldPassword())) {
            log.info("User old password matches");
            user.setPassword(input.getNewPassword());
            userRepo.saveAndFlush(user);
            log.info("Update user password");
            return true;
        }
        return false;
    }

    public Map<UUID, User> getUserOfPost(List<UUID> userIds) {
        log.info("Id {}", userIds.get(0));
        User user = userRepo.getById(userIds.get(0)).mapToUserType();
        log.info("user {}", user.getUsername());
        var mapis = userIds.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(),
                        id -> userRepo.getById(id).mapToUserType(),
                        (a, b) -> a,
                        ConcurrentHashMap::new));

        log.info("Map: {}", mapis);
        return mapis;
    }

    public Boolean userExitsById(UUID id) {
        return userRepo.existsById(id);
    }
}