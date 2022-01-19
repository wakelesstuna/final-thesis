package io.wakelesstuna.imagecdnserver.resoruce;

import io.wakelesstuna.imagecdnserver.application.AppConstants;
import io.wakelesstuna.imagecdnserver.application.ImageService;
import io.wakelesstuna.imagecdnserver.domain.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * A RestController class for the Image entity, it contains
 * endpoints for uploading, downloading and deleting images
 *
 * @author oscar.steen.forss
 */
@RestController
@RequestMapping(AppConstants.Paths.BASE_IMAGE_RESOURCE)
@RequiredArgsConstructor
@CrossOrigin("*")
public class ImageResource {

    private final ImageService imageService;

    /**
     * This endpoints handle the request for uploading a image
     * to the database. It forwarding it to the service to handle the
     * request.
     *
     * @param
     * @return String of the downloading url
     */
    @PostMapping(AppConstants.Paths.UPLOAD_FILE_RESOURCE + "/{userId}")
    public ResponseEntity<String> uploadFile(@PathVariable UUID userId, @RequestParam("file") MultipartFile file) {
        String imageUrl = imageService.uploadFile(userId, file);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(imageUrl);
    }

    /**
     * This endpoint handle the downloading of an image.
     *
     * @param imageId id of the image to download
     * @return Resource of the image
     */
    @GetMapping(value = AppConstants.Paths.DOWNLOAD_FILE_RESOURCE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable UUID imageId) {
        Image image = imageService.downloadImage(imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .body(new ByteArrayResource(image.getData()));
    }

    /**
     * This endpoint handles the deleting of an image from the database.
     *
     * @param imageId id of the image to delete
     * @param userId  owner id of the image to delete
     * @return String of the http status
     */
    @PostMapping(value = AppConstants.Paths.DELETE_FILE_RESOURCE + "/{imageId}/{userId}")
    public ResponseEntity<String> deleteImage(@PathVariable UUID imageId, @PathVariable UUID userId) {
        String response = imageService.deleteImage(imageId, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
