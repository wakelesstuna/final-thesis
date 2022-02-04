package io.wakelesstuna.postdgs.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link BookmarkEntity}
 *
 * @author oscar.steen.forss
 */
@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, UUID> {
    /**
     * Counts all the {@link BookmarkEntity} of a user.
     *
     * @param userId id of the user.
     * @return int
     */
    int countAllByUserId(UUID userId);

    /**
     * Finds a {@link BookmarkEntity} by user id and post id
     *
     * @param userId id of the user.
     * @param postId id of the post.
     * @return BookmarkEntity
     */
    Optional<BookmarkEntity> findByUserIdAndPostId(UUID userId, UUID postId);

    /**
     * Finds all bookmarkEntities by post id.
     *
     * @param postId UUID is of the post.
     * @return List of BookmarkEntity
     */
    List<BookmarkEntity> findAllByPostId(UUID postId);

    /**
     * Finds all bookmarkEntities by a list of user ids.
     *
     * @param ids List of user ids.
     * @return List of BookmarkEntity.
     */
    List<BookmarkEntity> findAllByUserIdIn(List<UUID> ids);

    /**
     * Check if a post exists by user id and post id.
     *
     * @param userId UUID of a user.
     * @param postId UUID of a post.
     * @return Boolean
     */
    Boolean existsByUserIdAndPostId(UUID userId, UUID postId);

    /**
     * Deletes all bookmarks by id.
     *
     * @param id UUID the user id that wants to delete all the bookmarks for.
     */
    void deleteAllByUserId(UUID id);
}


