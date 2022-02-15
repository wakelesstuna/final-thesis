package io.wakelesstuna.userdgs.datafetchers;

import com.netflix.graphql.dgs.*;
import io.wakelesstuna.user.generated.DgsConstants;
import io.wakelesstuna.user.generated.types.*;
import io.wakelesstuna.userdgs.dataloader.FollowersDataLoader;
import io.wakelesstuna.userdgs.dataloader.FollowingDataLoader;
import io.wakelesstuna.userdgs.services.FollowService;
import io.wakelesstuna.userdgs.services.UserService;
import lombok.AllArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This is data fetcher for the user type.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@AllArgsConstructor
public class UserDataFetcher {

    private final UserService userService;
    private final FollowService followService;

    @DgsQuery
    public User user(@InputArgument UUID id) {
        return userService.getUser(id);
    }

    @DgsQuery
    public List<User> users(@InputArgument String usernameFilter) {
        List<User> userList = userService.getUsers();
        if (usernameFilter == null) return userList;
        return userList.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(usernameFilter.toLowerCase()))
                .collect(Collectors.toList());
    }

    @DgsQuery
    public List<User> fetchRandomUsers(@InputArgument Integer howMany, @InputArgument String username) {
        return userService.getRandomUsers(howMany, username);
    }

    @DgsMutation
    public User createUser(@InputArgument CreateUserInput createUserInput, DgsDataFetchingEnvironment dfe) {
        MultipartFile file = dfe.getArgument("input");
        return userService.createUser(createUserInput, file);
    }

    @DgsMutation
    public User updateUser(@InputArgument UpdateUserInput updateUserInput, DgsDataFetchingEnvironment dfe) {
        MultipartFile file = dfe.getArgument("input");
        return userService.updateUser(updateUserInput, file);
    }

    @DgsMutation
    public Boolean updatePassword(@InputArgument UpdatePasswordInput updatePasswordInput) {
        return userService.updatePassword(updatePasswordInput);
    }

    @DgsMutation
    public User authUser(@InputArgument AuthUserInput authUserInput) {
        return userService.authenticateUser(authUserInput);
    }

    @DgsMutation
    public String deleteUser(@InputArgument AuthUserInput authUserInput) {
        return userService.deleteUser(authUserInput);
    }

    @DgsMutation
    public String followUser(@InputArgument FollowInput followInput) {
        return followService.followUser(followInput);
    }

    @DgsMutation
    public String unFollowUser(@InputArgument FollowInput followInput) {
        return followService.unFollowUser(followInput);
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME)
    public Integer totalFollowing(DgsDataFetchingEnvironment dfe) {
        User user = dfe.getSource();
        return followService.getTotalFollowing(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME)
    public CompletableFuture<User> following(DgsDataFetchingEnvironment dfe) {
        DataLoader<UUID, User> dataLoader = dfe.getDataLoader(FollowingDataLoader.class);
        User user = dfe.getSource();
        return dataLoader.load(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME)
    public Integer totalFollowers(DgsDataFetchingEnvironment dfe) {
        User user = dfe.getSource();
        return followService.getTotalFollowers(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME)
    public CompletableFuture<User> followers(DgsDataFetchingEnvironment dfe) {
        DataLoader<UUID, User> dataLoader = dfe.getDataLoader(FollowersDataLoader.class);
        User user = dfe.getSource();
        return dataLoader.load(user.getId());
    }

    @DgsQuery
    public Boolean userExitsById(@InputArgument UUID id) {
        return userService.userExitsById(id);
    }

    @DgsQuery
    public Boolean exitsByUsername(@InputArgument String username) {
        return userService.exitsByUsername(username);
    }

    @DgsQuery
    public Boolean exitsByEmail(@InputArgument String email) {
        return userService.exitsByEmail(email);
    }

    @DgsQuery
    public Boolean exitsByPhone(@InputArgument String phone) {
        return userService.exitsByPhone(phone);
    }
}
