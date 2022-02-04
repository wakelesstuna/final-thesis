package io.wakelesstuna.postdgs.exceptions;

import com.netflix.graphql.types.errors.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Custom general exception to be extended to other custom exceptions.
 *
 * @author oscar.steen.forss
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCustomException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;
    private int statusCode;
    private ErrorType errorType;
}
