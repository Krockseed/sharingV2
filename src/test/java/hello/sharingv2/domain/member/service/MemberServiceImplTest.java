package hello.sharingv2.domain.member.service;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.Role;
import hello.sharingv2.domain.member.dto.MemberDefaultDto;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import hello.sharingv2.domain.member.exception.MemberException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired MemberService memberService;
    @Autowired EntityManager em;

    @AfterEach
    void after() {
        em.flush();
        em.clear();
    }

    @Test
    void 회원가입_성공() throws Exception {
        //given
        MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "1234", "seed", "Kim");

        //when
        memberService.signUp(signUpDto);

        //then
        Member member = memberService.getMember(signUpDto.username());
        assertEquals(member.getUsername(), signUpDto.username());
        assertNotEquals(member.getPassword(), signUpDto.password());
        assertEquals(member.getName(), signUpDto.name());
        assertEquals(member.getNickname(), signUpDto.nickname());
        assertEquals(member.getAuthority(), Role.USER);
    }

    @Test
    void 회원탈퇴_성공() throws Exception {
        //given
        MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "1234", "seed", "Kim");
        MemberDefaultDto defaultDto = new MemberDefaultDto("ww@mail", "1234", "1234", "seed");

        //when
        memberService.signUp(signUpDto);
        memberService.signOut(defaultDto);

        //then
        assertThrows(MemberException.class, () -> memberService.getMember("ww@mail"));
    }

    @Test
    void 회원가입_실패() throws Exception {
        //given
        MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "1234", "seed", "Kim");
        MemberDefaultDto defaultDto = new MemberDefaultDto("ww@mail", "1234", "fail password", "seed");

        //when
        memberService.signUp(signUpDto);

        //then
        assertThrows(MemberException.class, () -> memberService.signOut(defaultDto));
    }
}