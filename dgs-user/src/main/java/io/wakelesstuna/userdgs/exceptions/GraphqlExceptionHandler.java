package io.wakelesstuna.userdgs.exceptions;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.types.errors.ErrorType;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;

import java.util.Map;

/**
 * This class is used to override the default behavior of the DataFetcherExceptionHandler.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
public class GraphqlExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {

        if (handlerParameters.getException() instanceof MyCustomException) {
            MyCustomException exception = (MyCustomException) handlerParameters.getException();
            Map<String, Object> debugInfo = Map.of(
                    "httpStatus", exception.getHttpStatus(),
                    "statusCode", exception.getHttpStatus().value());
            TypedGraphQLError graphQLError = TypedGraphQLError.newInternalErrorBuilder()
                    .message(exception.getMessage())
                    .path(handlerParameters.getPath())
                    .errorType(exception.getErrorType())
                    .debugInfo(debugInfo)
                    .build();
            return DataFetcherExceptionHandlerResult.newResult()
                    .error(graphQLError)
                    .build();
        }
        if (handlerParameters.getException() instanceof RuntimeException) {
            RuntimeException exception = (RuntimeException) handlerParameters.getException();
            Map<String, Object> map = Map.of(
                    "message", exception.getMessage()
            );
            TypedGraphQLError graphQLError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("Internal Server Error")
                    .path(handlerParameters.getPath())
                    .debugInfo(map)
                    .errorType(ErrorType.INTERNAL)
                    .build();
            return DataFetcherExceptionHandlerResult.newResult()
                    .error(graphQLError)
                    .build();
        }
        return null;
    }
}
