package hello.sharingv2.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final int errorCode;

    public ErrorResponse(HttpStatus status, String message, int errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    public static ErrorResponse toResponse(BaseExceptionType exceptionType) {
        return new ErrorResponse(
                exceptionType.getHttpStatus(),
                exceptionType.getErrorMessage(),
                exceptionType.getErrorCode()
        );
    }
}
