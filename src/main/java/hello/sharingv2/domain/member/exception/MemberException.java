package hello.sharingv2.domain.member.exception;

import hello.sharingv2.global.exception.BaseException;
import hello.sharingv2.global.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberException extends BaseException {

    private final BaseExceptionType exceptionType;

//    public MemberException(BaseExceptionType exceptionType) {
//        this.exceptionType = exceptionType;
//    }
//
//    @Override
//    public BaseExceptionType getExceptionType() {
//        return exceptionType;
//    }
}
