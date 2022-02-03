package io.wakelesstuna.userdgs.exceptions;

import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.netflix.graphql.dgs.context.DgsContext;
import com.netflix.graphql.types.errors.ErrorType;
import graphql.ExecutionResult;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import io.wakelesstuna.user.generated.client.UsersGraphQLQuery;
import io.wakelesstuna.user.generated.client.UsersProjectionRoot;
import io.wakelesstuna.userdgs.datafetchers.UserDataFetcher;
import io.wakelesstuna.userdgs.scalar.DateTimeScalar;
import io.wakelesstuna.userdgs.scalar.UUIDScalar;
import io.wakelesstuna.userdgs.services.FollowService;
import io.wakelesstuna.userdgs.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author oscar.steen.forss
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {DgsAutoConfiguration.class, UserDataFetcher.class, UserService.class, FollowService.class, DateTimeScalar.class, UUIDScalar.class})
class GraphqlExceptionHandlerTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    UserService userService;
    @MockBean
    FollowService followService;

    GraphqlExceptionHandler graphqlExceptionHandler;
    GraphQLQueryRequest graphQLQueryRequest;


    @BeforeEach
    void setUp() {
        graphqlExceptionHandler = new GraphqlExceptionHandler();

        graphQLQueryRequest = new GraphQLQueryRequest(
                UsersGraphQLQuery.newRequest().build(),
                new UsersProjectionRoot().username()
        );
    }

    @Test
    void shouldReturnDataFetcherExceptionHandlerResult() {
        Mockito.when(userService.getUsers())
                .thenThrow(MyCustomException.builder().message("nothing to see here").build());

        ExecutionResult result = dgsQueryExecutor.execute(graphQLQueryRequest.serialize());

        assertNotNull(result.getErrors());
        assertEquals(result.getErrors().get(0).getMessage(), "io.wakelesstuna.userdgs.exceptions.MyCustomException: nothing to see here");
    }

    @Test
    void shouldReturnDataFetcherExceptionHandlerResult2() {
        Mockito.when(userService.getUsers())
                .thenThrow(new RuntimeException("nothing to see here"));

        ExecutionResult result = dgsQueryExecutor.execute(graphQLQueryRequest.serialize());

        assertNotNull(result.getErrors());
        assertEquals(result.getErrors().get(0).getMessage(), "java.lang.RuntimeException: nothing to see here");
    }

    @Test
    void shouldReturnDataFetcherExceptionHandlerResult3() {
    }
}