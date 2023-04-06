package hello.sharingv2.domain.member.dto;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.Role;

import java.util.ArrayList;

public record MemberSignUpDto(String username, String password, String nickname, String name) {

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
