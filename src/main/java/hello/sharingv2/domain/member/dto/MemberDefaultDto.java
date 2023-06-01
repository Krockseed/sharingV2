package hello.sharingv2.domain.member.dto;

import hello.sharingv2.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MemberDefaultDto(
        @Email String username,
        @NotBlank @Min(value = 4) String password,
        @NotBlank @Min(value = 4) String cPassword,
        @NotBlank @Min(value = 4) String nickname) {

    public Member toEntity() {
        return Member.builder().username(username).password(password).nickname(nickname).build();
    }

    public boolean invalidPassword() {
        return !password.equals(cPassword);
    }
}
