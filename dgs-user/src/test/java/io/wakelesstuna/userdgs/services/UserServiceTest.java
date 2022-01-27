package io.wakelesstuna.userdgs.services;

import graphql.GraphQLException;
import io.wakelesstuna.user.generated.types.*;
import io.wakelesstuna.userdgs.exceptions.MyCustomException;
import io.wakelesstuna.userdgs.persistence.FollowRepository;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import io.wakelesstuna.userdgs.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * @author oscar.steen.forss
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService userService;

    @Mock
    ServiceHelper serviceHelper;
    @Mock
    FollowRepository followRepo;
    @Mock
    UserRepository userRepo;

    UUID userOneId = UUID.fromString("473eea09-69f7-40bf-bea0-f3380e926ecd");
    UUID userTwoId = UUID.fromString("c203fdae-b889-41c0-8f2f-712e95a1efd6");
    UUID userThreeId = UUID.fromString("378da3d4-c138-48fa-a3dd-d62eafe48815");

    UserEntity user;
    List<UserEntity> users;

    @BeforeEach
    void setUp() {
        userService = new UserService(serviceHelper, followRepo, userRepo);

        user = UserEntity.builder()
                .id(userOneId)
                .username("testUser")
                .email("test@email.com")
                .phone("07070000000")
                .password("test00")
                .build();

        users = Arrays.asList(
                UserEntity.builder()
                        .id(userOneId)
                        .username("donald duck")
                        .email("donald@email.com")
                        .phone("07070000000")
                        .password("test00")
                        .build(),
                UserEntity.builder()
                        .id(userTwoId)
                        .username("mickey mouse")
                        .email("mickey@email.com")
                        .phone("07070000000")
                        .password("test00")
                        .build(),
                UserEntity.builder()
                        .id(userThreeId)
                        .username("goofy")
                        .email("goofy@email.com")
                        .phone("07070000000")
                        .password("test00")
                        .build());
    }

    @Test
    void shouldReturnListOfTypeUser() {
        Mockito.when(userRepo.findAll())
                .thenAnswer(invocation -> users);

        var expected = 3;
        var users = userService.getUsers();
        assertEquals(expected, users.size());
    }

    @Test
    void getUserShouldReturnUser() {
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> user);

        var expected = "testUser";
        var user = userService.getUser(userOneId);
        assertEquals(expected, user.getUsername());
    }

    @Test
    void createUserShouldReturnUserAndId() {
        var expected = "wakelesstuna";
        final String imageUrl = "https://fake.url.com/image";
        Mockito.when(serviceHelper.uploadImage(any(UUID.class), any(MultipartFile.class)))
                .thenAnswer(invocation -> imageUrl);
        Mockito.when(serviceHelper.getLocalDateTime())
                .thenAnswer(invocation -> LocalDateTime.now());
        Mockito.when(userRepo.saveAndFlush(any(UserEntity.class)))
                .thenAnswer(invocation -> user);
        MultipartFile file = new MockMultipartFile("test", (byte[]) null);
        CreateUserInput request = CreateUserInput.newBuilder()
                .username("wakelesstuna")
                .firstName("donald")
                .lastName("duck")
                .email("donald@email.com")
                .phone("07071231212")
                .password("test00")
                .build();
        User user = userService.createUser(request, file);
        assertNotNull(user);
        assertEquals(expected, user.getUsername());
    }

    @Test
    void createUserShouldThrowErrorIfUsernameMissing() {
        Mockito.doThrow(MyCustomException.class)
                .when(serviceHelper)
                .checkIfRequiredFieldsAreMissing(any(CreateUserInput.class));
        MultipartFile file = new MockMultipartFile("test", (byte[]) null);
        CreateUserInput request = CreateUserInput.newBuilder()
                .firstName("donald")
                .lastName("duck")
                .email("donald@email.com")
                .phone("07071231212")
                .password("test00")
                .build();
        assertThrows(MyCustomException.class, () -> userService.createUser(request, file));
        Mockito.verify(userRepo, Mockito.times(0)).saveAndFlush(any());
        Mockito.verify(serviceHelper, Mockito.times(0)).uploadImage(any(), any());
    }

    @Test
    void updateUserShouldUpdateUsername() {
        var expected = "wakelesstuna";
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> UserEntity.builder()
                        .id(userOneId)
                        .username("test")
                        .firstName("foo")
                        .lastName("boo")
                        .email("foo@email.com")
                        .phone("07071231212")
                        .description("This is a test description")
                        .profilePic("https://test.com/link/to/image")
                        .build());
        MultipartFile file = null;
        UpdateUserInput request = UpdateUserInput.newBuilder()
                .userId(userOneId)
                .username(expected)
                .build();
        User user = userService.updateUser(request, file);
        assertEquals(expected, user.getUsername());
        assertEquals("foo", user.getFirstName());
        assertEquals("boo", user.getLastName());
        assertEquals("foo@email.com", user.getEmail());
        assertEquals("07071231212", user.getPhone());
        assertEquals("This is a test description", user.getDescription());
        assertEquals("https://test.com/link/to/image", user.getProfilePic());
    }

    @Test
    void updateUserShouldUpdateFirstName() {
        var expected = "test";
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> UserEntity.builder()
                        .id(userOneId)
                        .username("wakelesstuna")
                        .firstName("foo")
                        .lastName("boo")
                        .email("foo@email.com")
                        .phone("07071231212")
                        .description("This is a test description")
                        .profilePic("https://test.com/link/to/image")
                        .build());
        MultipartFile file = null;
        UpdateUserInput request = UpdateUserInput.newBuilder()
                .userId(userOneId)
                .firstName(expected)
                .build();
        User user = userService.updateUser(request, file);
        assertEquals("wakelesstuna", user.getUsername());
        assertEquals(expected, user.getFirstName());
        assertEquals("boo", user.getLastName());
        assertEquals("foo@email.com", user.getEmail());
        assertEquals("07071231212", user.getPhone());
        assertEquals("This is a test description", user.getDescription());
        assertEquals("https://test.com/link/to/image", user.getProfilePic());
    }

    @Test
    void updateUserShouldUpdateLastName() {
        var expected = "test";
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> UserEntity.builder()
                        .id(userOneId)
                        .username("wakelesstuna")
                        .firstName("foo")
                        .lastName("boo")
                        .email("foo@email.com")
                        .phone("07071231212")
                        .description("This is a test description")
                        .profilePic("https://test.com/link/to/image")
                        .build());
        MultipartFile file = null;
        UpdateUserInput request = UpdateUserInput.newBuilder()
                .userId(userOneId)
                .lastName(expected)
                .build();
        User user = userService.updateUser(request, file);
        assertEquals("wakelesstuna", user.getUsername());
        assertEquals("foo", user.getFirstName());
        assertEquals(expected, user.getLastName());
        assertEquals("foo@email.com", user.getEmail());
        assertEquals("07071231212", user.getPhone());
        assertEquals("This is a test description", user.getDescription());
        assertEquals("https://test.com/link/to/image", user.getProfilePic());
    }

    @Test
    void updateUserShouldUpdateEmail() {
        var expected = "test@email.com";
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> UserEntity.builder()
                        .id(userOneId)
                        .username("wakelesstuna")
                        .firstName("foo")
                        .lastName("boo")
                        .email("foo@email.com")
                        .phone("07071231212")
                        .description("This is a test description")
                        .profilePic("https://test.com/link/to/image")
                        .build());
        MultipartFile file = null;
        UpdateUserInput request = UpdateUserInput.newBuilder()
                .userId(userOneId)
                .email(expected)
                .build();
        User user = userService.updateUser(request, file);
        assertEquals("wakelesstuna", user.getUsername());
        assertEquals("foo", user.getFirstName());
        assertEquals("boo", user.getLastName());
        assertEquals(expected, user.getEmail());
        assertEquals("07071231212", user.getPhone());
        assertEquals("This is a test description", user.getDescription());
        assertEquals("https://test.com/link/to/image", user.getProfilePic());
    }

    @Test
    void updateUserShouldUpdatePhone() {
        var expected = "0709993322";
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> UserEntity.builder()
                        .id(userOneId)
                        .username("wakelesstuna")
                        .firstName("foo")
                        .lastName("boo")
                        .email("foo@email.com")
                        .phone("07071231212")
                        .description("This is a test description")
                        .profilePic("https://test.com/link/to/image")
                        .build());
        MultipartFile file = null;
        UpdateUserInput request = UpdateUserInput.newBuilder()
                .userId(userOneId)
                .phone(expected)
                .build();
        User user = userService.updateUser(request, file);
        assertEquals("wakelesstuna", user.getUsername());
        assertEquals("foo", user.getFirstName());
        assertEquals("boo", user.getLastName());
        assertEquals("foo@email.com", user.getEmail());
        assertEquals(expected, user.getPhone());
        assertEquals("This is a test description", user.getDescription());
        assertEquals("https://test.com/link/to/image", user.getProfilePic());
    }

    @Test
    void updateUserShouldUpdateDescription() {
        var expected = "Updated description";
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> UserEntity.builder()
                        .id(userOneId)
                        .username("wakelesstuna")
                        .firstName("foo")
                        .lastName("boo")
                        .email("foo@email.com")
                        .phone("07071231212")
                        .description("This is a test description")
                        .profilePic("https://test.com/link/to/image")
                        .build());
        MultipartFile file = null;
        UpdateUserInput request = UpdateUserInput.newBuilder()
                .userId(userOneId)
                .description(expected)
                .build();
        User user = userService.updateUser(request, file);
        assertEquals("wakelesstuna", user.getUsername());
        assertEquals("foo", user.getFirstName());
        assertEquals("boo", user.getLastName());
        assertEquals("foo@email.com", user.getEmail());
        assertEquals("07071231212", user.getPhone());
        assertEquals(expected, user.getDescription());
        assertEquals("https://test.com/link/to/image", user.getProfilePic());
    }

    @Test
    void updateUserShouldUpdateProfilePic() {
        var expected = "https://test.com/link/to/updated/imageUrl";
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> UserEntity.builder()
                        .id(userOneId)
                        .username("wakelesstuna")
                        .firstName("foo")
                        .lastName("boo")
                        .email("foo@email.com")
                        .phone("07071231212")
                        .description("This is a test description")
                        .profilePic("https://test.com/link/to/image")
                        .build());
        MultipartFile file = null;
        UpdateUserInput request = UpdateUserInput.newBuilder()
                .userId(userOneId)
                .profilePic(expected)
                .build();
        User user = userService.updateUser(request, file);
        assertEquals("wakelesstuna", user.getUsername());
        assertEquals("foo", user.getFirstName());
        assertEquals("boo", user.getLastName());
        assertEquals("foo@email.com", user.getEmail());
        assertEquals("07071231212", user.getPhone());
        assertEquals("This is a test description", user.getDescription());
        assertEquals(expected, user.getProfilePic());
    }

    @Test
    void updateUserShouldUpdateProfilePicWhenFileIsPresent() {
        var expected = "https://test.com/link/to/updated/imageUrl";
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> UserEntity.builder()
                        .id(userOneId)
                        .username("wakelesstuna")
                        .firstName("foo")
                        .lastName("boo")
                        .email("foo@email.com")
                        .phone("07071231212")
                        .description("This is a test description")
                        .profilePic("https://test.com/link/to/image")
                        .build());
        Mockito.when(serviceHelper.uploadImage(any(), any()))
                .thenAnswer(invocation -> expected);
        MultipartFile file = new MockMultipartFile("test", (byte[]) null);
        UpdateUserInput request = UpdateUserInput.newBuilder()
                .userId(userOneId)
                .build();
        User user = userService.updateUser(request, file);
        assertEquals("wakelesstuna", user.getUsername());
        assertEquals("foo", user.getFirstName());
        assertEquals("boo", user.getLastName());
        assertEquals("foo@email.com", user.getEmail());
        assertEquals("07071231212", user.getPhone());
        assertEquals("This is a test description", user.getDescription());
        assertEquals(expected, user.getProfilePic());
    }

    @Test
    void authShouldReturnUser() {
        var expected = "test00";
        var request = AuthUserInput.newBuilder()
                .username("testUser")
                .password("test00")
                .build();
        Mockito.when(userRepo.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> Optional.of(user));
        var user = userService.authenticateUser(request);
        assertEquals(user.getPassword(), expected);
    }

    @Test
    void authShouldThrowErrorCauseOfNoUserFound() {
        var request = AuthUserInput.newBuilder()
                .username("testUser")
                .password("test00")
                .build();
        Mockito.when(userRepo.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> Optional.empty());
        assertThrows(GraphQLException.class, () -> userService.authenticateUser(request));
    }

    @Test
    void authShouldThrowErrorWhenPasswordNotMatching() {
        var request = AuthUserInput.newBuilder()
                .username("testUser")
                .password("wrongPassword!")
                .build();
        Mockito.when(userRepo.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> Optional.of(user));
        assertThrows(GraphQLException.class, () -> userService.authenticateUser(request));
    }

    @Test
    void authShouldThrowErrorWhenNoUserFoundWithStatusCode_UNAUTHORIZED() {
        var expected = HttpStatus.UNAUTHORIZED.toString();
        var request = AuthUserInput.newBuilder()
                .username("testUser")
                .password("test00")
                .build();
        Mockito.when(userRepo.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> Optional.empty());
        try {
            userService.authenticateUser(request);
        } catch (GraphQLException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void authShouldThrowErrorWhenPasswordNotMatchingWithStatusCode_UNAUTHORIZED() {
        var expected = HttpStatus.UNAUTHORIZED.toString();
        var request = AuthUserInput.newBuilder()
                .username("testUser")
                .password("wrongPassword!")
                .build();
        Mockito.when(userRepo.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> Optional.of(user));
        try {
            userService.authenticateUser(request);
        } catch (GraphQLException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void getRandomUsersShouldReturnListOfUsers() {
        var expected = 3;
        Mockito.when(userRepo.findRandomButNotWith(anyString(), anyInt()))
                .thenAnswer(invocation -> users);

        var users = userService.getRandomUsers(3,"wakelesstuna");
        assertEquals(expected, users.size());
    }

    @Test
    void userExitsByIdShouldReturnTrue() {
        Mockito.when(userRepo.existsById(any(UUID.class)))
                .thenAnswer(invocation -> true);

        assertTrue(userService.userExitsById(userOneId));
    }

    @Test
    void userExitsByUsernameShouldReturnTrue() {
        Mockito.when(userRepo.existsByUsername(anyString()))
                .thenAnswer(invocation -> true);

        assertTrue(userService.exitsByUsername("testUser"));
    }

    @Test
    void userExitsByEmailShouldReturnTrue() {
        Mockito.when(userRepo.existsByEmail(anyString()))
                .thenAnswer(invocation -> true);
        assertTrue(userService.exitsByEmail("test@email.com"));
    }

    @Test
    void userExitsByPhoneShouldReturnTrue() {
        Mockito.when(userRepo.existsByPhone(anyString()))
                .thenAnswer(invocation -> true);
        assertTrue(userService.exitsByPhone("07072131212"));
    }

    @Test
    void updatePasswordShouldReturnTrue() {
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> user);
        Mockito.when(userRepo.saveAndFlush(any()))
                .thenAnswer(invocation -> user);
        var request = UpdatePasswordInput.newBuilder()
                .userId(userOneId)
                .oldPassword("test00")
                .newPassword("00test")
                .build();
        assertTrue(userService.updatePassword(request));
    }

    @Test
    void updatePasswordShouldReturnFalseWhenOldPasswordsDontMatch() {
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> user);
        var request = UpdatePasswordInput.newBuilder()
                .userId(userOneId)
                .oldPassword("thisIsNotTheOldPassword")
                .newPassword("00test")
                .build();
        assertFalse(userService.updatePassword(request));
        Mockito.verify(userRepo, Mockito.times(0)).saveAndFlush(any());
    }

    @Test
    void getPostOfUserShouldReturnMapOfIdAndUser() {
        var exceptedSize = 2;
        var exceptedIdOne = userOneId;
        var exceptedIdTwo = userTwoId;
        Mockito.when(userRepo.getById(any(UUID.class)))
                .thenAnswer(invocation -> user);

        var userOfPost = userService.getUserOfPost(Arrays.asList(userOneId, userTwoId));
        assertEquals(exceptedSize, userOfPost.size());
        assertTrue(userOfPost.containsKey(exceptedIdOne));
        assertTrue(userOfPost.containsKey(exceptedIdTwo));
    }

    @Test
    void testMergeFunctionOnCollectorsShouldReturnSizeOneForMap() {
        var exceptedSize = 1;
        var exceptedIdOne = userOneId;
        var notExceptedIdTwo = userTwoId;
        Mockito.when(userRepo.getById(any(UUID.class)))
                .thenAnswer(invocation -> user);

        var userOfPost = userService.getUserOfPost(Arrays.asList(userOneId, userOneId));
        assertEquals(exceptedSize, userOfPost.size());
        assertTrue(userOfPost.containsKey(exceptedIdOne));
        assertFalse(userOfPost.containsKey(notExceptedIdTwo));
    }

    @Test
    void deleteUserShouldReturnHttpStatus_ACCEPTED() {
        var expected = HttpStatus.ACCEPTED.toString();
        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> user);
        var request = AuthUserInput.newBuilder()
                .userId(userOneId)
                .username("wakelesstuna")
                .password("test00")
                .build();
        var actual = userService.deleteUser(request);
        assertEquals(expected,actual);
        Mockito.verify(followRepo, Mockito.times(1)).deleteAllByUserId(any(UUID.class));
        Mockito.verify(followRepo, Mockito.times(1)).deleteAllByFollowId(any(UUID.class));
        Mockito.verify(userRepo, Mockito.times(1)).delete(any(UserEntity.class));
    }

    @Test
    void sendDeleteRequest() {
        // TODO: 2022-01-26 Fix this test
        userService.sendDeleteRequest();
    }
}