package io.wakelesstuna.userdgs.services;

import graphql.GraphQLException;
import io.wakelesstuna.user.generated.types.CreateUserInput;
import io.wakelesstuna.userdgs.exceptions.MyCustomException;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import io.wakelesstuna.userdgs.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * @author oscar.steen.forss
 */
@ExtendWith(MockitoExtension.class)
class ServiceHelperTest {

    ServiceHelper serviceHelper;

    @Mock
    UserRepository userRepo;
    @Mock
    RestTemplate restTemplate;
    String imageServiceBaseUrl = "http://localhost:8005";

    UUID userId = UUID.fromString("473eea09-69f7-40bf-bea0-f3380e926ecd");

    UserEntity user;

    @BeforeEach
    void setUp() {
        serviceHelper = new ServiceHelper(Clock.systemUTC(), userRepo, restTemplate, imageServiceBaseUrl);
        user = UserEntity.builder()
                .id(userId)
                .username("testUser")
                .firstName("foo")
                .lastName("boo")
                .email("test@email.com")
                .phone("0701231212")
                .build();
    }

    @Test
    void shouldReturnLocalDateTime() {
        assertNotNull(serviceHelper.getLocalDateTime());
    }

    @Test
    void shouldUploadImageAndReturnDownloadUrl() throws IOException {
        Path path = Paths.get("src/test/resources/test-image.jpg");
        byte[] content = Files.readAllBytes(path);
        MultipartFile file = new MockMultipartFile("test-image", "test-image.jpg", "image/jpg", content);
        Mockito.when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenAnswer(invocation -> ResponseEntity.ok("http://localhost:8005/cdn/server/v1/download"));
        var response = serviceHelper.uploadImage(userId, file);
        assertNotNull(response);
    }

    @Test
    void shouldUploadAndGenerateAvatarAndReturnDownloadUrl() {
        Mockito.when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenAnswer(invocation -> ResponseEntity.ok("http://localhost:8005/cdn/server/v1/download"));
        var response = serviceHelper.uploadImage(userId, null);
        assertNotNull(response);
    }

    @Test
    void shouldReturnRightMessageWhenServiceIsUnavailable() {
        var expected = "Service unavailable, could not upload image";
        Mockito.doThrow(ResourceAccessException.class)
                .when(restTemplate)
                .postForEntity(anyString(), any(), any());
        try {
            serviceHelper.uploadImage(userId, null);
        } catch (GraphQLException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldReturnUser() {
        Mockito.when(userRepo.findById(userId))
                .thenAnswer(invocation -> Optional.of(user));

        assertNotNull(serviceHelper.getUser(userId));
    }

    @Test
    void shouldThrowGraphQLErrorWhenUserNotFound() {
        Mockito.when(userRepo.findById(userId))
                .thenAnswer(invocation -> Optional.empty());

        assertThrows(GraphQLException.class, () -> serviceHelper.getUser(userId));
    }

    @Test
    void shouldThrowErrorIfUsernameAlreadyExits() {
        var expected = "Username already exists";
        Mockito.when(userRepo.existsByUsername("testUser"))
                .thenAnswer(invocation -> true);
        try {
            serviceHelper.checkIfUsernameUnique(user.getUsername());
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldThrowErrorIfUsernameInputIsEmpty() {
        var expected = "Email cannot be empty";
        try {
            serviceHelper.checkIfEmailUnique("");
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldThrowErrorIfEmailAlreadyExits() {
        var expected = "Email already exists";
        Mockito.when(userRepo.existsByEmail("test@email.com"))
                .thenAnswer(invocation -> true);
        try {
            serviceHelper.checkIfEmailUnique(user.getEmail());
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldThrowErrorIfEmailInputIsEmpty() {
        var expected = "Email cannot be empty";
        try {
            serviceHelper.checkIfPhoneUnique("");
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldThrowErrorIfPhoneAlreadyExits() {
        var expected = "Phone number already exists";
        Mockito.when(userRepo.existsByPhone("0701231212"))
                .thenAnswer(invocation -> true);
        try {
            serviceHelper.checkIfPhoneUnique(user.getPhone());
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldThrowErrorIfPasswordIsEmpty() {
        var expected = "Password cannot be empty";
        try {
            serviceHelper.validatePassword("");
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }

    }

    @Test
    void shouldThrowErrorIfPasswordIsNotCorrectLength() {
        var expected = "Password must be al least 6 characters long";
        try {
            serviceHelper.validatePassword("test");
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldNotThrowErrorWhenPasswordIsCorrectly() {
        assertDoesNotThrow(() ->  serviceHelper.validatePassword("test0000"));
    }

    @Test
    void shouldThrowErrorWhenImageSizeToBig() {
        var expected = "File size to big. Max size allowed 2 MB";
        try {
            serviceHelper.checkIfImageSize(4000000L);
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldNotThrowErrorWhenImageSizeNotToBig() {
        assertDoesNotThrow(() -> serviceHelper.checkIfImageSize(3000L));
    }

    @Test
    void shouldThrowErrorWhenDescriptionIsToLong() {
        var expected = "Description is to long max size 225 characters";
        var toLongDescription = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.";
        try {
            serviceHelper.checkIfDescriptionIsValid(toLongDescription);
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldNotThrowErrorWhenNoRequiredFieldsAreMissing() {
        CreateUserInput input = new CreateUserInput();
        input.setUsername("testUser");
        input.setEmail("test@email.com");
        input.setPassword("test00");
        assertDoesNotThrow(() -> serviceHelper.checkIfRequiredFieldsAreMissing(input));
    }

    @Test
    void shouldThrowErrorIfUsernameIsNull() {
        var expected = "Username cannot be null";
        CreateUserInput input = new CreateUserInput();
        input.setEmail("test@email.com");
        input.setPassword("test00");
        try {
            serviceHelper.checkIfRequiredFieldsAreMissing(input);
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldThrowErrorIfEmailIsNull() {
        var expected = "Email cannot be null";
        CreateUserInput input = new CreateUserInput();
        input.setUsername("testUser");
        input.setPassword("test00");
        try {
            serviceHelper.checkIfRequiredFieldsAreMissing(input);
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void shouldThrowErrorIfPasswordIsNull() {
        var expected = "Password cannot be null";
        CreateUserInput input = new CreateUserInput();
        input.setUsername("testUser");
        input.setEmail("test@email.com");
        try {
            serviceHelper.checkIfRequiredFieldsAreMissing(input);
        } catch (MyCustomException e) {
            assertEquals(expected, e.getMessage());
        }
    }
}