package hello.sharingv2.domain.member.dto;

import hello.sharingv2.domain.member.Member;

public record MemberDefaultDto(String username, String password, String cPassword, String nickname) {

    public Member toEntity() {
        return Member.builder().username(username).password(password).nickname(nickname).build();
    }

    public boolean invalidPassword() {
        return !password.equals(cPassword);
    }
}
