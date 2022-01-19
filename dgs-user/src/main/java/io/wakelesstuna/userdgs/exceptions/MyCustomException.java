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
@NoArgsConstructor
@Getter
public class MyCustomException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;
    private int statusCode;
    private ErrorType errorType;

}
