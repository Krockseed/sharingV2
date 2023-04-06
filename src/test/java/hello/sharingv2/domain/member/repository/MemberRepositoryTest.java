package hello.sharingv2.domain.member.repository;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    /**
     * 회원관련 test 시 주의해야 할 사항
     * 1. 회원 저장 시 속성값이 없다면 오류
     * 2. 회원 수정
     * 3. 회원 삭제
     * 4. 회원 찾기
     * 5. baseTimeEntity 작동 여부
     */

    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @AfterEach
    void after() {
        em.clear();
    }

    @Test
    void 회원저장_성공() throws Exception {
        //given
        Member member = Member.builder()
                .username("username").password("1234")
                .nickname("hello").name("Kim")
                .authority(Role.USER).build();

        //when
        Member saveMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(saveMember.getId())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        assertEquals(member, findMember);
        assertEquals(member, saveMember);
    }

    @Test
    void 회원가입시_이름존재_X() throws Exception {
        //given
        Member member = Member.builder()
                .password("1234").nickname("hello")
                .name("Kim").authority(Role.USER).build();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member));
    }

    @Test
    void 중복회원_오류() throws Exception {
        //given
        Member member = Member.builder()
                .username("username").password("1234")
                .nickname("hello").name("Kim")
                .authority(Role.USER).build();
        Member member2 = Member.builder()
                .username("username").password("1234")
                .nickname("hello").name("Kim")
                .authority(Role.USER).build();

        //when
        memberRepository.save(member);
        em.clear();

        //then
        assertThrows(Exception.class, () -> memberRepository.save(member2));
    }

    @Test
    void 회원생성시_BaseTime_저장성공() throws Exception {
        //given
        Member member = Member.builder()
                .username("username").password("1234")
                .nickname("hello").name("Kim")
                .authority(Role.USER).build();

        //when
        Member save = memberRepository.save(member);

        //then
        assertNotNull(save.getCreatedAt());
        assertNotNull(save.getLastModifiedAt());
    }
}