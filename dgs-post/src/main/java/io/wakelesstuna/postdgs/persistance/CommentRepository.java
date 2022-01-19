package io.wakelesstuna.postdgs.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link CommentEntity}
 *
 * @author oscar.steen.forss
 */
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    /**
     * Counts all the {@link CommentEntity} for a post.
     *
     * @param postId id of the post.
     * @return int
     */
    int countAllByPostId(UUID postId);

    /**
     * Finds a {@link CommentEntity} for a post.
     *
     * @param postId id of the post.
     * @param id     id of the comment.
     * @return CommentEntity
     */
    Optional<CommentEntity> findByPostIdAndId(UUID postId, UUID id);

    List<CommentEntity> findAllByPostId(UUID postId);
    /**
     * Deletes all {@link CommentEntity} by user id.
     *
     * @param userId id of the user to delete all comments from.
     */
    void deleteAllByUserId(UUID userId);
}

