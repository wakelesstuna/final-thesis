package io.wakelesstuna.imagecdnserver.application;

import io.wakelesstuna.imagecdnserver.domain.Image;
import io.wakelesstuna.imagecdnserver.persistance.ImageRepository;
import io.wakelesstuna.imagecdnserver.resoruce.dto.ImageFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This is a service that handle logic for uploading and
 * loading image data from the database.
 *
 * @author oscar.steen.forss
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageService {

    private final Clock clock;
    private final ImageRepository imageRepo;
    @Value("${image.service.base-url}")
    private String baseUrl;

    /**
     * Uploads a file to the database, sets important data
     * to the entity and then returns the download url for the image.
     *
     * @param userId UUID
     * @param file   MultipartFile
     * @return String
     */
    public String uploadFile(UUID userId, MultipartFile file) {
        UUID imageId = UUID.randomUUID();
        String url = generateDownloadUrl(imageId);
        try {
            Image newImage = Image.builder()
                    .id(imageId)
                    .ownerId(userId)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .data(file.getBytes())
                    .createdAt(LocalDateTime.now(clock))
                    .build();

            imageRepo.saveAndFlush(newImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("New image was uploaded with url: {}", url);
        return url;
    }

    /**
     * Persists a image file to the database.
     *
     * @param imageFile ImageFile
     * @return String
     */
    public String uploadFile(ImageFile imageFile) {
        UUID imageId = UUID.randomUUID();
        String url = generateDownloadUrl(imageId);

        Image newImage = Image.builder()
                .id(imageId)
                .ownerId(imageFile.getUserId())
                .fileName(imageFile.getFileName())
                .fileType(imageFile.getFileType())
                .fileSize(imageFile.getFileSize())
                .data(imageFile.getData())
                .createdAt(LocalDateTime.now(clock))
                .build();
        imageRepo.saveAndFlush(newImage);

        log.info("New image was uploaded with url: {}", url);
        return url;
    }

    /**
     * Fetches the image entity with the corresponding id
     *
     * @param imageId of the image
     * @return Image
     */
    public Image downloadImage(UUID imageId) {
        Image image = imageRepo.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File not found"));
        log.info("Fetch image: {}", image.getId());
        return image;
    }

    /**
     * Deletes a Image from the database
     *
     * @param imageId id of the image to delete
     * @param userId  owner id of the image to delete
     * @return String
     */
    public String deleteImage(UUID imageId, UUID userId) {
        Image image = imageRepo.findByIdAndOwnerId(imageId, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong ids"));
        imageRepo.delete(image);
        log.info("Delete image: {}", image.getId());
        return HttpStatus.ACCEPTED.toString();
    }

    /**
     * This method generates an download url for an image
     *
     * @param imageId id of the image that needs and url
     * @return String
     */
    private String generateDownloadUrl(UUID imageId) {
        return String.format("%s%s%s/%s",
                baseUrl,
                AppConstants.Paths.BASE_IMAGE_RESOURCE,
                AppConstants.Paths.DOWNLOAD_FILE_RESOURCE.substring(0, AppConstants.Paths.DOWNLOAD_FILE_RESOURCE.lastIndexOf('/')),
                imageId);
    }
}

