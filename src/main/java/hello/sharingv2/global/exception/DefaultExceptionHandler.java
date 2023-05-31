package hello.sharingv2.global.exception;

import hello.sharingv2.domain.member.exception.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    @ExceptionHandler({MemberException.class})
    protected ResponseEntity memberException(MemberException e) {
        log.error("MemberException : ", e);

        BaseExceptionType ex = e.getExceptionType();
        return new ResponseEntity<>(new ErrorResponse(ex.getHttpStatus(), ex.getErrorMessage(), ex.getErrorCode()), ex.getHttpStatus());
    }
}
