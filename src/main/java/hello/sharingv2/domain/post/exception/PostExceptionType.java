package hello.sharingv2.domain.post.exception;

import hello.sharingv2.global.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PostExceptionType implements BaseExceptionType {

    NOT_FOUND_POST(600, HttpStatus.OK, "글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(610, HttpStatus.OK, "댓글을 찾을 수 없습니다"),
    NOT_FOUND_TAG(620, HttpStatus.OK, "태그를 찾을 수 없습니다.");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
