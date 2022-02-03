package io.wakelesstuna.userdgs.exceptions;

import com.netflix.graphql.types.errors.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * This class is used to create custom exceptions.
 *
 * @author oscar.steen.forss
 */
@Builder
@AllArgsConstructor
@Getter
public class MyCustomException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;
    private final int statusCode;
    private final ErrorType errorType;

}
