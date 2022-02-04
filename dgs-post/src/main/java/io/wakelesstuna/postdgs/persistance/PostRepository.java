package io.wakelesstuna.postdgs.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link PostEntity}
 *
 * @author oscar.steen.forss
 */
@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    /**
     * Counts all PostEntities of a user by user id.
     *
     * @param userId UUID id of the user.
     * @return Integer.
     */
    Integer countAllByUserId(UUID userId);

    /**
     * Fetches a list of PostEntities by user id.
     *
     * @param userId UUID id of the user.
     * @return List of PostEntity.
     */
    List<PostEntity> findAllByUserId(UUID userId);

    /**
     * Fetches a PostEntity by user id and post id.
     *
     * @param userId UUID id of the user.
     * @param postId UUID id of the post.
     * @return Optional of PostEntity
     */
    Optional<PostEntity> findByUserIdAndId(UUID userId, UUID postId);

    /**
     * Fetches a list of PostEntities by a list of user ids.
     *
     * @param ids List of user ids to fetch post for.
     * @return List of PostEntity
     */
    List<PostEntity> findByUserIdIn(List<UUID> ids);

    /**
     * Deletes all post by user id.
     *
     * @param id UUID id of the user to delete all post for.
     */
    void deleteAllByUserId(UUID id);


}
