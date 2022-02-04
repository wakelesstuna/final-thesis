package io.wakelesstuna.postdgs.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author oscar.steen.forss
 */
@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    Integer countAllByUserId(UUID userId);
    List<PostEntity> findAllByUserId(UUID userId);
    Optional<PostEntity> findByUserIdAndId(UUID userId, UUID postId);


    List<PostEntity> findByUserIdIn(List<UUID> ids);

    void deleteAllByUserId(UUID id);


}
