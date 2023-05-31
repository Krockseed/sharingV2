package hello.sharingv2.domain.member.service;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.dto.MemberDefaultDto;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;

public interface MemberService {

    void signUp(MemberSignUpDto signUpDto);

    void signOut(MemberDefaultDto defaultDto);

    void update(MemberDefaultDto defaultDto);

    Member getMember(String email);
}
