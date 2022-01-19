package io.wakelesstuna.userdgs.services;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.types.errors.ErrorType;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.GitHubAvatar;
import com.talanlabs.avatargenerator.layers.backgrounds.ColorPaintBackgroundLayer;
import graphql.GraphQLException;
import io.wakelesstuna.userdgs.exceptions.MyCustomException;
import io.wakelesstuna.userdgs.persistence.UserRepository;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import io.wakelesstuna.userdgs.services.dto.ImageFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
@DgsComponent
public class ServiceHelper {

    private final Clock clock;

    @Value("${image.service.base-url}")
    private String imageServiceBaseUrl;
    private final String IMAGE_SERVICE_UPLOAD_FILE_RESOURCE = "/image-service/api/v1/upload";
    @Value("${image.max-size}")
    private Long maxImageSize = 2L;

    private final UserRepository userRepo;
    private final RestTemplate restTemplate = new RestTemplate();

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
     * @param file multipart file to upload
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

    public UserEntity getUser(UUID userId) {
        return userRepo.findById(userId).orElseThrow(() -> new GraphQLException("No user found"));
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

    public boolean userExists(UUID userId) {
        return userRepo.existsById(userId);
    }

    public void checkIfUsernameUnique(String username) {
        if (username.isEmpty())
            throw new MyCustomException("Username cannot be empty", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        if (userRepo.existsByUsername(username))
            throw new MyCustomException("Username already exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    public void checkIfEmailUnique(String email) {
        if (email.isEmpty())
            throw new MyCustomException("Email cannot be empty", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        if (userRepo.existsByEmail(email))
            throw new MyCustomException("Email already exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    public void checkIfPhoneUnique(String phone) {
        if (phone.isEmpty()) return;
        if (userRepo.existsByPhone(phone))
            throw new MyCustomException("Phone number already exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    public void validatePassword(String password) {
        if (password.isEmpty())
            throw new MyCustomException("Password cannot be empty", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        if (password.length() < 6)
            throw new MyCustomException("Password must be al least 6 characters long", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    public void checkIfImageSize(Long fileSize) {
        long fileSizeInMB = fileSize / 1000000;

        if (fileSizeInMB > maxImageSize)
            throw new MyCustomException("File size to big. Max size allowed " + maxImageSize + " MB", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    public void checkIfDescriptionIsValid(String description) {
        if (description.length() > 225) {
            throw new MyCustomException("Description is to long max size 225 characters", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        }
    }
}
