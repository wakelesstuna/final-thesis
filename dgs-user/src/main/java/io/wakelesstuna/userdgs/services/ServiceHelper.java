package io.wakelesstuna.userdgs.services;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.types.errors.ErrorType;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.GitHubAvatar;
import com.talanlabs.avatargenerator.layers.backgrounds.ColorPaintBackgroundLayer;
import graphql.GraphQLException;
import io.wakelesstuna.user.generated.types.CreateUserInput;
import io.wakelesstuna.userdgs.exceptions.MyCustomException;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import io.wakelesstuna.userdgs.persistence.UserRepository;
import io.wakelesstuna.userdgs.services.dto.ImageFile;
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
@DgsComponent
public class ServiceHelper {

    private final Clock clock;
    private final UserRepository userRepo;
    private final RestTemplate restTemplate;
    private final String imageServiceBaseUrl;

    public ServiceHelper(Clock clock, UserRepository userRepo, RestTemplate restTemplate, @Value("${image.service.base-url}") String imageServiceBaseUrl) {
        this.clock = clock;
        this.userRepo = userRepo;
        this.restTemplate = restTemplate;
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

        final String IMAGE_SERVICE_UPLOAD_FILE_RESOURCE = "/cdn/server/v1/upload";
        final String URL_FOR_IMAGE_SERVICE = imageServiceBaseUrl + IMAGE_SERVICE_UPLOAD_FILE_RESOURCE;
        log.info("Image service uri: {}", URL_FOR_IMAGE_SERVICE);

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
            answer = restTemplate.postForEntity(URL_FOR_IMAGE_SERVICE, imageFile, String.class);
        } catch (ResourceAccessException e) {
            final String errorMsg = "Service unavailable, could not upload image";
            log.error(errorMsg, e);
            throw new GraphQLException(errorMsg);
        } catch (IOException e) {
            log.error("IOException when uploading file", e);
            throw new GraphQLException(e.getMessage());
        }
        log.info("Image ur: {}", answer.getBody());
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

    /**
     * Fetching a user.
     *
     * @param userId Id of the user to fetch.
     * @return UserEntity
     */
    public UserEntity getUser(UUID userId) {
        return userRepo.findById(userId).orElseThrow(() -> new GraphQLException("No user found"));
    }

    /**
     * Check if a username is unique and does not already exists in the database.
     *
     * @param username String
     */
    public void checkIfUsernameUnique(String username) {
        if (username.isEmpty())
            throw new MyCustomException("Username cannot be empty", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        if (userRepo.existsByUsername(username))
            throw new MyCustomException("Username already exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    /**
     * Check if a email is unique and does not already exists in the database.
     *
     * @param email String
     */
    public void checkIfEmailUnique(String email) {
        if (email.isEmpty())
            throw new MyCustomException("Email cannot be empty", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        if (userRepo.existsByEmail(email))
            throw new MyCustomException("Email already exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    /**
     * Check if a phone is unique and does not already exists in the database.
     *
     * @param phone String
     */
    public void checkIfPhoneUnique(String phone) {
        if (userRepo.existsByPhone(phone))
            throw new MyCustomException("Phone number already exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    /**
     * Validates a password.
     *
     * @param password String
     */
    public void validatePassword(String password) {
        if (password.isEmpty())
            throw new MyCustomException("Password cannot be empty", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        if (password.length() < 6)
            throw new MyCustomException("Password must be al least 6 characters long", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    /**
     * Check if the image size is allowed to be stored.
     *
     * @param fileSize Long
     */
    public void checkIfImageSize(Long fileSize) {
        long fileSizeInMB = fileSize / 1000000;
        final long maxImageSizeInMB = 2L;

        if (fileSizeInMB > maxImageSizeInMB)
            throw new MyCustomException("File size to big. Max size allowed " + maxImageSizeInMB + " MB", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
    }

    /**
     * Validates the length of the description.
     *
     * @param description String
     */
    public void checkIfDescriptionIsValid(String description) {
        if (description.length() > 225) {
            throw new MyCustomException("Description is to long max size 225 characters", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        }
    }

    /**
     * Check if any of the required fields are missing to be able to create a user.
     *
     * @param input CreateUSerInput
     */
    public void checkIfRequiredFieldsAreMissing(CreateUserInput input) {
        if (input.getUsername() == null) {
            throw new MyCustomException("Username cannot be null", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        }
        if (input.getEmail() == null) {
            throw new MyCustomException("Email cannot be null", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        }
        if (input.getPassword() == null) {
            throw new MyCustomException("Password cannot be null", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);
        }
    }
}
