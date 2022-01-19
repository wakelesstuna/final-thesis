package io.wakelesstuna.imagecdnserver.persistance;

import io.wakelesstuna.imagecdnserver.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Image object.
 *
 * @author oscar.steen.forss
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    /**
     * Fetches a Image entity from the database
     *
     * @param imageId id of the image to fetch
     * @param userId  ownerId of the image to fetch
     * @return an Optional of an Image object
     */
    Optional<Image> findByIdAndOwnerId(UUID imageId, UUID userId);
}
