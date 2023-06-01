package hello.sharingv2.domain.member.dto;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;

public record MemberSignUpDto(
        @Email String username,
        // Validation 관련, String -> @Size, Integer -> @Min, @Max
        @NotBlank @Size(min = 4, max = 15) String password,
        @NotBlank @Size(min = 4, max = 15) String nickname,
        @NotBlank @Size(min = 3, max = 15) String name) {

    /**
     * 새로운 기능 추가될 때마다 new ArrayList<>() 를 통해 빈 객체를 생성해줘야 한다
     */
    public Member toEntity() {
        return Member.builder().username(username).authority(Role.USER)
                .password(password).nickname(nickname).name(name)
                .posts(new ArrayList<>())
                .comments(new ArrayList<>()).build();
    }
}
