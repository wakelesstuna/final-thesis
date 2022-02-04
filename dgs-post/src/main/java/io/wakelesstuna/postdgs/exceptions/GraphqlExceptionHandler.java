package io.wakelesstuna.postdgs.exceptions;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.types.errors.ErrorType;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to configure custom behavior of the exceptions cast in the datafetchers.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@Slf4j
public class GraphqlExceptionHandler implements DataFetcherExceptionHandler {


    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {

        if (handlerParameters.getException() instanceof MyCustomException) {
            MyCustomException exception = (MyCustomException) handlerParameters.getException();

            if (exception.getClass().isAssignableFrom(PostNotFoundException.class)) {
                log.error("Post not found");
            }else if (exception.getClass().isAssignableFrom(UserNotFoundException.class)) {
                log.error("User not found");
            }
            TypedGraphQLError graphQLError = buildError(exception, handlerParameters);
            return DataFetcherExceptionHandlerResult.newResult()
                    .error(graphQLError)
                    .build();
        }

        if (handlerParameters.getException() instanceof RuntimeException) {
            RuntimeException exception = (RuntimeException) handlerParameters.getException();
            log.error("Internal Server Error {}", exception.getMessage());
            log.error("Internal Server Error {}", exception.getClass().toString());

            TypedGraphQLError graphQLError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("Internal Server Error")
                    .path(handlerParameters.getPath())
                    .errorType(ErrorType.INTERNAL)
                    .build();

            return DataFetcherExceptionHandlerResult.newResult()
                    .error(graphQLError)
                    .build();
        }

        return null;
    }

    private TypedGraphQLError buildError(MyCustomException e, DataFetcherExceptionHandlerParameters handlerParameters) {
        return TypedGraphQLError.newInternalErrorBuilder()
                .message(e.getMessage())
                .path(handlerParameters.getPath())
                .errorType(e.getErrorType())
                .debugInfo(buildDebugInfo(e))
                .build();
    }

    private Map<String, Object> buildDebugInfo(MyCustomException e) {
            return Map.of(
                "message", e.getMessage(),
                "httpStatus", e.getHttpStatus(),
                "errorType", e.getErrorType()
        );
    }
}
