package hello.sharingv2.domain.post.exception;

import hello.sharingv2.global.exception.BaseException;
import hello.sharingv2.global.exception.BaseExceptionType;

public class PostException extends BaseException {

    private final BaseExceptionType exceptionType;

    public PostException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
