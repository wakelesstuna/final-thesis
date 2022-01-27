package io.wakelesstuna.userdgs.datafetchers;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.netflix.graphql.dgs.exceptions.QueryException;
import graphql.schema.Coercing;
import io.wakelesstuna.user.generated.client.*;
import io.wakelesstuna.user.generated.types.*;
import io.wakelesstuna.userdgs.scalar.DateTimeScalar;
import io.wakelesstuna.userdgs.scalar.UUIDScalar;
import io.wakelesstuna.userdgs.services.FollowService;
import io.wakelesstuna.userdgs.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


/**
 * @author oscar.steen.forss
 */
@SpringBootTest(classes = {DgsAutoConfiguration.class, UserDataFetcher.class, DateTimeScalar.class, UUIDScalar.class})
class UserDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    UserService userService;

    @MockBean
    FollowService followService;

    Map<Class<?>, Coercing<?, ?>> scalars = new HashMap<>();

    UUID userId = UUID.fromString("473eea09-69f7-40bf-bea0-f3380e926ecd");

    @BeforeEach
    void setUp() {
        scalars.put(UUID.class, new UUIDScalar());
        scalars.put(LocalDateTime.class, new DateTimeScalar());

        User user = User.newBuilder()
                .id(userId)
                .username("testUser")
                .email("test@email.com")
                .phone("07070000000")
                .password("test00")
                .build();

        User updatedUser = User.newBuilder()
                .id(userId)
                .username("goofy")
                .email("test@email.com")
                .phone("07070000000")
                .password("test00")
                .build();

        List<User> users = Arrays.asList(
                User.newBuilder()
                        .id(userId)
                        .username("donald duck")
                        .email("donald@email.com")
                        .phone("07070000000")
                        .password("test00")
                        .build(),
                User.newBuilder()
                        .id(userId)
                        .username("mickey mouse")
                        .email("mickey@email.com")
                        .phone("07070000000")
                        .password("test00")
                        .build(),
                User.newBuilder()
                        .id(userId)
                        .username("goofy")
                        .email("goofy@email.com")
                        .phone("07070000000")
                        .password("test00")
                        .build());

        Mockito.when(userService.createUser(any(CreateUserInput.class), any()))
                .thenAnswer(invocation -> user);

        Mockito.when(userService.getUser(any(UUID.class)))
                .thenAnswer(invocation -> user);

        Mockito.when(userService.getUsers())
                .thenAnswer(invocation -> users);

        Mockito.when(userService.getRandomUsers(any(), any()))
                .thenAnswer(invocation -> users);

        Mockito.when(userService.updateUser(any(UpdateUserInput.class), any()))
                .thenAnswer(invocation -> updatedUser);

        Mockito.when(userService.updatePassword(any(UpdatePasswordInput.class)))
                .thenAnswer(invocation -> true);

        Mockito.when(userService.authenticateUser(any(AuthUserInput.class)))
                .thenAnswer(invocation -> user);
    }

    @Test
    void shouldReturnUser() {
        var expected = "testUser";
        var graphQLQueryRequest = new GraphQLQueryRequest(
                UserGraphQLQuery.newRequest().id(userId).build(),
                new UserProjectionRoot().username(),
                scalars
        );
        var user = dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.user", User.class);
        assertEquals(user.getUsername(), expected);
    }

    @Test
    void shouldReturnListOfAllUsers() {
        var usernameOne = "donald duck";
        var usernameTwo = "mickey mouse";
        var usernameThree = "goofy";

        var graphQLQueryRequest = new GraphQLQueryRequest(
                UsersGraphQLQuery.newRequest().build(),
                new UsersProjectionRoot().username()
        );
        List<String> usernames = dgsQueryExecutor.executeAndExtractJsonPath(graphQLQueryRequest.serialize(), "data.users[*].username");
        assertTrue(usernames.contains(usernameOne));
        assertTrue(usernames.contains(usernameTwo));
        assertTrue(usernames.contains(usernameThree));
    }

    @Test
    void shouldReturnListOfOnlyGoofy() {
        var usernameOne = "donald duck";
        var usernameTwo = "mickey mouse";
        var usernameThree = "goofy";

        var graphQLQueryRequest = new GraphQLQueryRequest(
                UsersGraphQLQuery.newRequest().usernameFilter("goofy").build(),
                new UsersProjectionRoot().username()
        );
        List<String> usernames = dgsQueryExecutor.executeAndExtractJsonPath(graphQLQueryRequest.serialize(), "data.users[*].username");
        assertFalse(usernames.contains(usernameOne));
        assertFalse(usernames.contains(usernameTwo));
        assertTrue(usernames.contains(usernameThree));
    }

    @Test
    void shouldReturnListOfThreeUsers() {
        var excepted = 3;
        var graphQLQueryRequest = new GraphQLQueryRequest(
                FetchRandomUsersGraphQLQuery.newRequest().howMany(3).username("testUser").build(),
                new UserProjectionRoot().username()
        );
        List<String> users = dgsQueryExecutor.executeAndExtractJsonPath(graphQLQueryRequest.serialize(), "data.fetchRandomUsers[*].username");
        assertEquals(excepted, users.size());
    }

    @Test
    void shouldReturnThrowErrorCauseMissingInputParameterUsername() {
        var graphQLQueryRequest = new GraphQLQueryRequest(
                FetchRandomUsersGraphQLQuery.newRequest().howMany(3).build(),
                new UserProjectionRoot().username()
        );
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPath(graphQLQueryRequest.serialize(), "data.fetchRandomUsers[*].username"));
    }

    @Test
    void createUserShouldReturnUserObject() {
        var excepted = "testUser";
        var graphQLQueryRequest = new GraphQLQueryRequest(
                CreateUserGraphQLQuery.newRequest()
                        .createUserInput(CreateUserInput.newBuilder()
                                .username(excepted)
                                .email("test@email.com")
                                .password("test00")
                                .build()).build(),
                new UserProjectionRoot().username()
        );
        var user = dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.createUser", User.class);
        assertEquals(user.getUsername(), excepted);
    }

    @Test
    void createUserShouldThrowErrorCauseMissingPasswordField() {
        var graphQLQueryRequest = new GraphQLQueryRequest(
                CreateUserGraphQLQuery.newRequest()
                        .createUserInput(CreateUserInput.newBuilder()
                                .username("testUser")
                                .email("test@email.com")
                                .build()).build(),
                new UserProjectionRoot().username()
        );
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.user", User.class));
    }

    @Test
    void createUserShouldThrowErrorCauseMissingUsernameField() {
        var graphQLQueryRequest = new GraphQLQueryRequest(
                CreateUserGraphQLQuery.newRequest()
                        .createUserInput(CreateUserInput.newBuilder()
                                .email("test@email.com")
                                .password("test00")
                                .build()).build(),
                new UserProjectionRoot().username()
        );
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.user", User.class));
    }

    @Test
    void createUserShouldThrowErrorCauseMissingEmailField() {
        var excepted = "testUser";
        var graphQLQueryRequest = new GraphQLQueryRequest(
                CreateUserGraphQLQuery.newRequest()
                        .createUserInput(CreateUserInput.newBuilder()
                                .username(excepted)
                                .password("test@email.com")
                                .build()).build(),
                new UserProjectionRoot().username()
        );
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.user", User.class));
    }

    @Test
    void updateUserShouldReturnUser() {
        var excepted = "goofy";
        var graphQLQueryRequest = new GraphQLQueryRequest(
                UpdateUserGraphQLQuery.newRequest()
                        .updateUserInput(UpdateUserInput.newBuilder()
                                .userId(userId)
                                .username(excepted)
                                .build()).build(),
                new UserProjectionRoot().username(),
                scalars
        );
        var user = dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.updateUser", User.class);
        assertEquals(user.getUsername(), excepted);
    }

    @Test
    void updateUserShouldThrowErrorWhenUserIdIsMissing() {
        var excepted = "donald";
        var graphQLQueryRequest = new GraphQLQueryRequest(
                UpdateUserGraphQLQuery.newRequest()
                        .updateUserInput(UpdateUserInput.newBuilder()
                                .username(excepted)
                                .build()).build(),
                new UserProjectionRoot().username()
        );
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.updateUser", User.class));
    }

    @Test
    void updatePasswordShouldReturnTrue() {
        var graphQLQueryRequest = new GraphQLQueryRequest(
                UpdatePasswordGraphQLQuery.newRequest()
                        .updatePasswordInput(
                                UpdatePasswordInput.newBuilder()
                                        .userId(userId)
                                        .oldPassword("test00")
                                        .newPassword("password")
                                        .build()).build(),
                null,
                scalars);
        Boolean response = dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.updatePassword", Boolean.class);
        assertTrue(response);
    }

    @Test
    void updatePasswordShouldThrowErrorWhenUserIdMissing() {
        var graphQLQueryRequest = new GraphQLQueryRequest(
                UpdatePasswordGraphQLQuery.newRequest()
                        .updatePasswordInput(
                                UpdatePasswordInput.newBuilder()
                                        .oldPassword("test00")
                                        .newPassword("password")
                                        .build()).build(),
                null,
                scalars);
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.updatePassword", Boolean.class));
    }

    @Test
    void updatePasswordShouldThrowErrorWhenNewPasswordMissing() {
        var graphQLQueryRequest = new GraphQLQueryRequest(
                UpdatePasswordGraphQLQuery.newRequest()
                        .updatePasswordInput(
                                UpdatePasswordInput.newBuilder()
                                        .userId(userId)
                                        .oldPassword("test00")
                                        .build()).build(),
                null,
                scalars);
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQLQueryRequest.serialize(), "data.updatePassword", Boolean.class));
    }

    @Test
    void updatePasswordShouldThrowErrorWhenOldPasswordMissing() {
        var query = new GraphQLQueryRequest(
                UpdatePasswordGraphQLQuery.newRequest()
                        .updatePasswordInput(
                                UpdatePasswordInput.newBuilder()
                                        .userId(userId)
                                        .newPassword("password")
                                        .build()).build(),
                null,
                scalars).serialize();
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.updatePassword", Boolean.class));
    }

    @Test
    void authUserShouldReturnUser() {
        var query = new GraphQLQueryRequest(
                AuthUserGraphQLQuery.newRequest()
                .authUserInput(AuthUserInput.newBuilder()
                        .username("test")
                        .password("test00")
                        .build()).build(),
                new UserProjectionRoot().username(),
                scalars
        ).serialize();
        var user = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.authUser",User.class);
        assertNotNull(user);
    }

    @Test
    void authUserShouldThrowErrorWhenUsernameMissing() {
        var query = new GraphQLQueryRequest(
                AuthUserGraphQLQuery.newRequest()
                        .authUserInput(AuthUserInput.newBuilder()
                                .password("test00")
                                .build()).build(),
                new UserProjectionRoot().username(),
                scalars
        ).serialize();
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.authUser",User.class));
    }

    @Test
    void authUserShouldThrowErrorWhenPasswordMissing() {
        var query = new GraphQLQueryRequest(
                AuthUserGraphQLQuery.newRequest()
                        .authUserInput(AuthUserInput.newBuilder()
                                .username("test")
                                .build()).build(),
                new UserProjectionRoot().username(),
                scalars
        ).serialize();
        assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.authUser",User.class));
    }
}