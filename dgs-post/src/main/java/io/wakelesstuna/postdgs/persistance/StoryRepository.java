package io.wakelesstuna.postdgs.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoryRepository extends JpaRepository<StoryEntity, UUID> {
    List<StoryEntity> findAllByUserId(UUID id);
    Optional<StoryEntity> findByUserIdAndId(UUID userId, UUID id);
}