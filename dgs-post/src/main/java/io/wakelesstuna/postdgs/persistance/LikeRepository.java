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

    List<LikeEntity> findAllByPostId(UUID postId);

    boolean existsByUserIdAndAndPostId(UUID userId, UUID postId);

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
     * @return int
     */
    int countAllByPostId(UUID postId);

    /**
     * Deletes all {@link LikeEntity} with the same user id.
     *
     * @param userId id of the user.
     */
    void deleteAllByUserId(UUID userId);
}
