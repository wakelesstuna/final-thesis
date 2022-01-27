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
 * @author oscar.steen.forss
 */
@DgsComponent
@AllArgsConstructor
public class UserDataFetcher {

    private final UserService userService;
    private final FollowService followService;

    @DgsData(parentType = DgsConstants.QUERY_TYPE,field = DgsConstants.QUERY.User)
    public User user(@InputArgument UUID id) {
        return userService.getUser(id);
    }

    @DgsQuery(field = DgsConstants.QUERY.Users)
    public List<User> users(@InputArgument String usernameFilter) {
        List<User> userList = userService.getUsers();
        if (usernameFilter == null) return userList;
        return userList.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(usernameFilter.toLowerCase()))
                .collect(Collectors.toList());
    }

    @DgsQuery(field = DgsConstants.QUERY.FetchRandomUsers)
    public List<User> getRandomUser(@InputArgument Integer howMany, @InputArgument String username) {
        return userService.getRandomUsers(howMany, username);
    }

    @DgsMutation(field = DgsConstants.MUTATION.CreateUser)
    public User createUser(@InputArgument CreateUserInput createUserInput, DgsDataFetchingEnvironment dfe) {
        MultipartFile file = dfe.getArgument("input");
        return userService.createUser(createUserInput, file);
    }

    @DgsMutation(field = DgsConstants.MUTATION.UpdateUser)
    public User updateUser(@InputArgument UpdateUserInput updateUserInput, DgsDataFetchingEnvironment dfe) {
        MultipartFile file = dfe.getArgument("input");
        return userService.updateUser(updateUserInput, file);
    }

    @DgsMutation(field = DgsConstants.MUTATION.UpdatePassword)
    public Boolean updatePassword(@InputArgument UpdatePasswordInput updatePasswordInput) {
        return userService.updatePassword(updatePasswordInput);
    }

    @DgsMutation(field = DgsConstants.MUTATION.AuthUser)
    public User authUser(@InputArgument AuthUserInput authUserInput) {
        return userService.authenticateUser(authUserInput);
    }

    @DgsMutation(field = DgsConstants.MUTATION.DeleteUser)
    public String deleteUser(@InputArgument AuthUserInput authUserInput) {
        return userService.deleteUser(authUserInput);
    }

    @DgsMutation(field = DgsConstants.MUTATION.FollowUser)
    public String followUser(@InputArgument FollowInput followInput) {
        return followService.followUser(followInput);
    }

    @DgsMutation(field = DgsConstants.MUTATION.UnFollowUser)
    public String unFollowUser(@InputArgument FollowInput followInput) {
        return followService.unFollowUser(followInput);
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME, field = DgsConstants.USER.TotalFollowing)
    public Integer totalFollowing(DgsDataFetchingEnvironment dfe) {
        User user = dfe.getSource();
        return followService.getTotalFollowing(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME, field = DgsConstants.USER.Following)
    public CompletableFuture<User> following(DgsDataFetchingEnvironment dfe) {
        DataLoader<UUID, User> dataLoader = dfe.getDataLoader(FollowingDataLoader.class);
        User user = dfe.getSource();
        return dataLoader.load(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME, field = DgsConstants.USER.TotalFollowers)
    public Integer totalFollowers(DgsDataFetchingEnvironment dfe) {
        User user = dfe.getSource();
        return followService.getTotalFollowers(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME, field = DgsConstants.USER.Followers)
    public CompletableFuture<User> followers(DgsDataFetchingEnvironment dfe) {
        DataLoader<UUID, User> dataLoader = dfe.getDataLoader(FollowersDataLoader.class);
        User user = dfe.getSource();
        return dataLoader.load(user.getId());
    }

    @DgsQuery(field = DgsConstants.QUERY.UserExitsById)
    public Boolean userExitsById(@InputArgument UUID id) {
        return userService.userExitsById(id);
    }

    @DgsQuery(field = DgsConstants.QUERY.ExitsByUsername)
    public Boolean exitsByUsername(@InputArgument String username) {
        return userService.exitsByUsername(username);
    }

    @DgsQuery(field = DgsConstants.QUERY.ExitsByEmail)
    public Boolean exitsByEmail(@InputArgument String email) {
        return userService.exitsByEmail(email);
    }

    @DgsQuery(field = DgsConstants.QUERY.ExitsByPhone)
    public Boolean exitsByPhone(@InputArgument String phone) {
        return userService.exitsByPhone(phone);
    }
}
