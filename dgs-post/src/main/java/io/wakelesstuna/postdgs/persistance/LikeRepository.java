package io.wakelesstuna.postdgs.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link LikeEntity}
 *
 * @author oscar.steen.forss
 */
@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, UUID> {

    /**
     * Fetches a list of LikeEntities by post id.
     *
     * @param postId UUID id of the post to find likeEntities for.
     * @return List of LikeEntity.
     */
    List<LikeEntity> findAllByPostId(UUID postId);

    /**
     * Finds a {@link LikeEntity} by postId and userId.
     *
     * @param postId id of the post.
     * @param userId id of the user.
     * @return LikeEntity
     */
    LikeEntity findByPostIdAndUserId(UUID postId, UUID userId);

    /**
     * Counts all {@link LikeEntity} for a post by id.
     *
     * @param postId id of post.
     * @return Integer
     */
    Integer countAllByPostId(UUID postId);

    /**
     * Checks if a like exists by user id and post id.
     *
     * @param userId UUID of the user.
     * @param postId UUID of the post.
     * @return Boolean
     */
    Boolean existsByUserIdAndPostId(UUID userId, UUID postId);

    /**
     * Deletes all {@link LikeEntity} with the same user id.
     *
     * @param userId id of the user.
     */
    void deleteAllByUserId(UUID userId);
}
