package io.wakelesstuna.userdgs.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link FollowEntity}
 *
 * @author oscar.steen.forss
 */
@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, UUID> {

    /**
     * Finds all {@link FollowEntity} by user id.
     *
     * @param userId id of the user to find who is following.
     * @return List of FollowEntity
     */
    List<FollowEntity> getAllByUserId(UUID userId);

    /**
     * Finds all {@link FollowEntity} by user id.
     *
     * @param followId id of the user to find followers for.
     * @return List of FollowEntity
     */
    List<FollowEntity> getAllByFollowId(UUID followId);

    /**
     * Fetches a {@link FollowEntity} by user id and follow id.
     *
     * @param userId   id of the user.
     * @param followId id of the follower.
     * @return FollowEntity
     */
    Optional<FollowEntity> getFollowByUserIdAndFollowId(UUID userId, UUID followId);

    /**
     * Counts all follow id.
     *
     * @param followId id of follow.
     * @return int
     */
    int countAllByFollowId(UUID followId);

    /**
     * Counts all user id.
     *
     * @param userId id of user.
     * @return int
     */
    int countAllByUserId(UUID userId);

    /**
     * Deletes all {@link FollowEntity} by user id.
     *
     * @param userId id of the user.
     */
    void deleteAllByUserId(UUID userId);

    /**
     * Deletes all {@link FollowEntity} by follow id.
     *
     * @param followId id of the user.
     */
    void deleteAllByFollowId(UUID followId);
}

