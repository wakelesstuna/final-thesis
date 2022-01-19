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


    boolean existsByUserIdAndAndPostId(UUID userId, UUID postId);

    List<BookmarkEntity> findAllByUserId(UUID userId);
    List<BookmarkEntity> findAllByPostId(UUID postId);
}


