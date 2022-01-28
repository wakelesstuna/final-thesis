package io.wakelesstuna.userdgs.services;

import com.netflix.graphql.dgs.DgsComponent;
import graphql.GraphQLException;
import io.wakelesstuna.user.generated.types.FollowInput;
import io.wakelesstuna.user.generated.types.User;
import io.wakelesstuna.userdgs.persistence.FollowEntity;
import io.wakelesstuna.userdgs.persistence.FollowRepository;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import io.wakelesstuna.userdgs.persistence.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This is a service class that handle the logic for the {@link FollowEntity}.
 *
 * @author oscar.steen.forss
 */
@Slf4j
@AllArgsConstructor
@DgsComponent
public class FollowService {

    private final ServiceHelper serviceHelper;
    private final FollowRepository followRepo;
    private final UserRepository userRepo;

    /**
     * Fetching two users from the database, creates
     * a Follow entity and persist it to the database.
     *
     * @param followInput the id of the user and the follow id
     * @return String
     */
    public String followUser(FollowInput followInput) {
        UserEntity user = userRepo.getById(followInput.getUserId());
        UserEntity follow = userRepo.getById(followInput.getFollowId());

        FollowEntity newFollowEntity = FollowEntity.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .followId(follow.getId())
                .createdAt(serviceHelper.getLocalDateTime())
                .build();

        followRepo.saveAndFlush(newFollowEntity);
        log.info("New follow added {}", newFollowEntity.getId());
        return String.format("user %s started following user %s", user.getId(), follow.getId());
    }

    /**
     * Creates a Follow entity that maps 2 users together.
     *
     * @param followInput user id and the user id of the user to follow
     * @return String
     */
    public String unFollowUser(FollowInput followInput) {
        FollowEntity followEntity = followRepo.getFollowByUserIdAndFollowId(followInput.getUserId(), followInput.getFollowId())
                .orElseThrow(() -> new GraphQLException("User does not follow current user"));

        followRepo.delete(followEntity);
        log.info("User {} stopped follow user {}", followEntity.getUserId(), followEntity.getFollowId());
        return "Stopped following user";
    }

    /**
     * This method counts all that follows a user
     * and returns the count.
     *
     * @param id id of the user that you wanna get the follow count of
     * @return Integer
     */
    public Integer getTotalFollowers(UUID id) {
        return followRepo.countAllByFollowId(id);
    }

    /**
     * This method counts all the users that a user is following
     * and returns the count.
     *
     * @param id id of the user that you wanna get the following count of
     * @return Integer
     */
    public Integer getTotalFollowing(UUID id) {
        return followRepo.countAllByUserId(id);
    }


    /**
     * This is the method we want to call when loading followers for multiple users.
     * If this code was backed by a relational database, it would select
     * reviews for all requested shows in a single SQL query.
     */
    public Map<UUID, List<User>> followersForUsers(List<UUID> userIds) {
        log.info("Loading followers for {} users", userIds.size());

        Map<UUID, List<User>> map = new ConcurrentHashMap<>();

        for (UUID id : userIds) {
            List<UUID> followersIds = followRepo.getAllByFollowId(id).stream()
                    .map(FollowEntity::getUserId)
                    .collect(Collectors.toList());
            map.put(id, getUsers(followersIds));
        }
        return map;
    }

    public Map<UUID, List<User>> followingsForUsers(List<UUID> userIds) {

        Map<UUID, List<User>> map = new ConcurrentHashMap<>();

        for (UUID id : userIds) {
            List<UUID> followingIds = followRepo.getAllByUserId(id).stream()
                    .map(FollowEntity::getFollowId)
                    .collect(Collectors.toList());
            map.put(id, getUsers(followingIds));
        }
        return map;
    }

    @NotNull
    private List<User> getUsers(List<UUID> usersIds) {
        return userRepo.findAllById(usersIds).stream()
                .map(UserEntity::mapToUserType)
                .collect(Collectors.toList());
    }


}
