package io.wakelesstuna.userdgs.exceptions;

import com.netflix.graphql.types.errors.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
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
