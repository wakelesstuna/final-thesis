package io.wakelesstuna.userdgs.services;

import com.netflix.graphql.dgs.DgsComponent;
import graphql.GraphQLException;
import io.wakelesstuna.user.generated.types.*;
import io.wakelesstuna.userdgs.persistence.FollowRepository;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import io.wakelesstuna.userdgs.persistence.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a service class that handle the logic for the {@link UserEntity}.
 *
 * @author oscar.steen.forss
 */
@Slf4j
@AllArgsConstructor
@DgsComponent
public class UserService {

    private final ServiceHelper serviceHelper;
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
        UUID userId = UUID.randomUUID();
        serviceHelper.checkIfRequiredFieldsAreMissing(input);
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

    /**
     * Updates a user information
     *
     * @param input UpdateUserInput object with information about what to update.
     * @param file  MultipartFile to update profile image
     * @return User the updated user
     */
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
        } else if (input.getProfilePic() != null) {
            userToUpdate.setProfilePic(input.getProfilePic());
        }

        userRepo.saveAndFlush(userToUpdate);
        log.info("Saving user updates: {}", userToUpdate.mapToUserType().toString());
        return userToUpdate.mapToUserType();
    }

    /**
     * Updates a user password.
     *
     * @param input UpdateUserInput input about the user to update the password for.
     * @return Boolean
     */
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

    /**
     * Deletes a user from the database. Sends request to post service to delete the user
     * posts, comments, likes, and bookmarks.
     *
     * @param authUserInput AuthUserInput input about the user to delete.
     * @return String
     */
    @Transactional
    public String deleteUser(AuthUserInput authUserInput) {
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

    /**
     * Sends a delete request to the post service.
     */
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

    /**
     * Authenticate a user, returns the user if success.
     *
     * @param authUserInput input about the user to authenticate.
     * @return User
     */
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

    /**
     * Fetches a random list of users.
     *
     * @param howMany  Integer, how many random users to fetch.
     * @param username String, username of a user to exclude.
     * @return List of user.
     */
    public List<User> getRandomUsers(Integer howMany, String username) {
        return userRepo.findRandomButNotWith(username, howMany).stream()
                .map(UserEntity::mapToUserType)
                .collect(Collectors.toList());
    }

    /**
     * Check if user exists by id.
     *
     * @param id of the user
     * @return Boolean
     */
    public Boolean userExitsById(UUID id) {
        return userRepo.existsById(id);
    }

    /**
     * Check if user exists by username.
     *
     * @param username of the user
     * @return Boolean
     */
    public Boolean exitsByUsername(String username) {
        log.info("Checking if username exists: {}", username);
        return userRepo.existsByUsername(username);
    }

    /**
     * Check if user exists by email.
     *
     * @param email of the user
     * @return Boolean
     */
    public Boolean exitsByEmail(String email) {
        log.info("Checking if email exists: {}", email);
        return userRepo.existsByEmail(email);
    }

    /**
     * Check if user exists by phone.
     *
     * @param phone of the user
     * @return Boolean
     */
    public Boolean exitsByPhone(String phone) {
        log.info("Checking if email exists: {}", phone);
        return userRepo.existsByPhone(phone);
    }

    /**
     * Get a map of users, user id is used as key and the user as value.
     * @param userIds List of user ids.
     * @return Map<UUID,User>
     */
    public Map<UUID, User> getUserOfPost(List<UUID> userIds) {
        List<UserEntity> allByIds = userRepo.findAllById(userIds);
        return userIds.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(),
                        id -> allByIds.stream().filter(u -> u.getId().equals(id)).findFirst()
                        .map(UserEntity::mapToUserType)
                        .get(),
                        (a, b) -> a,
                        ConcurrentHashMap::new));
    }
}