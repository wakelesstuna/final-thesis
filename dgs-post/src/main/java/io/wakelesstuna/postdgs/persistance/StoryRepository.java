package io.wakelesstuna.postdgs.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Repository for {@link StoryEntity}
 *
 * @author oscar.steen.forss
 */
@Repository
public interface StoryRepository extends JpaRepository<StoryEntity, UUID> {

    /**
     * Fetches a StoryEntity by user id and story id.
     *
     * @param userId UUID id of the user.
     * @param id     UUID id of the story.
     * @return Optional StoryEntity.
     */
    Optional<StoryEntity> findByUserIdAndId(UUID userId, UUID id);

    /**
     * Fetches a list of StoryEntities by a list of user ids.
     *
     * @param ids List of UUID ids.
     * @return List fo StoryEntity.
     */
    List<StoryEntity> findAllByUserIdIn(List<UUID> ids);

    /**
     * Deletes all StoryEntities by user id.
     *
     * @param id UUID id of the user.
     */
    void deleteAllByUserId(UUID id);
}