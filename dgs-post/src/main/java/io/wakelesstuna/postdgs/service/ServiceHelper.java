package io.wakelesstuna.postdgs.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.types.errors.ErrorType;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.GitHubAvatar;
import com.talanlabs.avatargenerator.layers.backgrounds.ColorPaintBackgroundLayer;
import io.wakelesstuna.postdgs.exceptions.MyCustomException;
import io.wakelesstuna.postdgs.exceptions.PostNotFoundException;
import io.wakelesstuna.postdgs.persistance.PostEntity;
import io.wakelesstuna.postdgs.persistance.PostRepository;
import io.wakelesstuna.postdgs.service.dto.ImageFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * This is a helper class for all the services. It holds methods
 * that are shared by the services
 *
 * @author oscar.steen.forss
 */
@Slf4j
@DgsComponent
public class ServiceHelper {

    private Clock clock;
    private PostRepository postRepo;
    private RestTemplate restTemplate;

    @Value("${image.max-size}")
    private Long maxImageSize = 2L;

    private String imageServiceBaseUrl;
    private final String IMAGE_SERVICE_UPLOAD_FILE_RESOURCE = "/cdn/server/v1/upload";
    private final String IMAGE_SERVICE_DELETE_FILE_RESOURCE = "/cdn/server/v1/delete";

    public ServiceHelper(Clock clock, PostRepository postRepo, @Value("${image.service.base-url}") String imageServiceBaseUrl) {
        this.clock = clock;
        this.postRepo = postRepo;
        this.restTemplate = new RestTemplate();
        this.imageServiceBaseUrl = imageServiceBaseUrl;
    }

    /**
     * Returns the LocalDateTime of now configured with a clock
     *
     * @return LocalDataTime
     */
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.now(clock);
    }

    /**
     * Converts a MultipartFile to and ImageFile and send it to
     * the image service for persistence.
     *
     * @param userId id of the user that uploaded the file
     * @param file   MultipartFile of the image
     * @return String the download url for the image
     */
    public String uploadImage(UUID userId, MultipartFile file) {
        ImageFile imageFile;
        ResponseEntity<String> answer;

        final String urlForImageService = imageServiceBaseUrl + IMAGE_SERVICE_UPLOAD_FILE_RESOURCE;
        log.info("Image service uri: {}", urlForImageService);

        try {
            if (file == null) {
                log.info("Multipart file not present, generating avatar image");
                imageFile = generateAvatarImageFile(userId);
            } else {
                log.info("Generating image file from multipart file");
                checkIfImageSize(file.getSize());
                imageFile = generateImageFile(userId, file);
            }

            log.info("Uploading file to image service");
            answer = restTemplate.postForEntity(urlForImageService, imageFile, String.class);
        } catch (ResourceAccessException e) {
            final String errorMsg = "Service unavailable, could not upload image";
            log.error(errorMsg);
            return "";
        } catch (IOException ioException) {
            return "";
        }
        return answer.getBody();
    }

    public String deleteImage(PostEntity post) {
        ResponseEntity<String> response = null;

        final String urlForImageService = buildDeleteUrl(post);
        log.info("Image service uri: {}", urlForImageService);

        try {
            response = restTemplate.postForEntity(urlForImageService, null, String.class);
        } catch (ResourceAccessException e) {
            final String errorMsg = "Service unavailable, could not delete image";
            log.error(errorMsg);
        }
        return response.getBody();
    }


    /**
     * Generates a random avatar, and creates an {@link ImageFile} object
     *
     * @param userId id of the user that uploaded the multipart file
     * @return ImageFile object
     */
    public ImageFile generateAvatarImageFile(UUID userId) {
        Avatar avatar = GitHubAvatar.newAvatarBuilder().layers(new ColorPaintBackgroundLayer(Color.WHITE)).build();
        return ImageFile.builder()
                .userId(userId)
                .fileName("random-avatar/" + UUID.randomUUID())
                .fileType("image/png")
                .fileSize(0L)
                .data(avatar.createAsPngBytes(new Random().nextLong()))
                .build();
    }

    /**
     * Generates an {@link ImageFile} from the multipart file and returns it.
     *
     * @param userId id of the user that uploaded the multipart file
     * @param file   multipart file to upload
     * @return ImageFile object
     * @throws IOException if reading byte stream goes wrong
     */
    public ImageFile generateImageFile(UUID userId, MultipartFile file) throws IOException {
        return ImageFile.builder()
                .userId(userId)
                .fileName(Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().lastIndexOf(".")))
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .data(file.getInputStream().readAllBytes())
                .build();
    }

    public PostEntity getPost(UUID postId) {
        return postRepo.findById(postId).orElseThrow(() -> PostNotFoundException.builder()
                .message("No post found")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errorType(ErrorType.BAD_REQUEST)
                .build());
    }

    /**
     * Check if a value is null returns true if value is null.
     *
     * @param value Object to check if null
     * @return boolean
     */
    public boolean checkIfValueIsNull(Object value) {
        return value == null;
    }

    public boolean postExists(UUID postId) {
        return postRepo.existsById(postId);
    }

    public void checkIfImageSize(Long fileSize) {
        long fileSizeInMB = fileSize / 1000000;

        if (fileSizeInMB > maxImageSize)
            throw new MyCustomException("File size to big. Max size allowed " + maxImageSize + " MB", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }


    public boolean doesUserExists(UUID userId) {
        // TODO: 2022-01-14 Send request to user.dgs to se if user exsits
        return true;
    }

    /**
     * Builds the url for deleting an image from the image cdn server
     *
     * @param post Post from were to delete image from
     * @return String
     */
    public String buildDeleteUrl(PostEntity post) {
        String imageId = extractImageId(post.getImageUrl());
        return imageServiceBaseUrl + IMAGE_SERVICE_DELETE_FILE_RESOURCE + "/" + imageId + "/" + post.getUserId();
    }

    /**
     * Extracts the image id from the url.
     * Cause we always knows that the second first path variable of the url
     * is the image id, we split the url on / and takes the next to last index
     * of the array as image id and sends it back.
     *
     * @param imageUrl Url for the image
     * @return String
     */
    public String extractImageId(String imageUrl) {
        String[] split = imageUrl.split("/");
        return split[split.length - 1];
    }

}
