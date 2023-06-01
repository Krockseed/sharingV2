package hello.sharingv2.global.exception;

import hello.sharingv2.domain.member.exception.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    @ExceptionHandler({MemberException.class})
    protected ResponseEntity memberException(MemberException e) {
        log.error("MemberException : {}", e.getExceptionType().getErrorMessage());

        BaseExceptionType ex = e.getExceptionType();
        return new ResponseEntity<>(new ErrorResponse(ex.getHttpStatus(), ex.getErrorMessage(), ex.getErrorCode()), ex.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity methodNotValid(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "잘못된 입력 형식입니다.", 400);

        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
