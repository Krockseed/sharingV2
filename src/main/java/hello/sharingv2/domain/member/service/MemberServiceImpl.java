package hello.sharingv2.domain.member.service;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.dto.MemberDefaultDto;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import hello.sharingv2.domain.member.exception.MemberException;
import hello.sharingv2.domain.member.exception.MemberExceptionType;
import hello.sharingv2.domain.member.repository.MemberRepository;
import hello.sharingv2.domain.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(MemberSignUpDto signUpDto) {
        if (memberRepository.existsByUsername(signUpDto.username())) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }
        if (memberRepository.existsByNickname(signUpDto.nickname())) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }

        Member member = signUpDto.toEntity();

        member.encodePassword(passwordEncoder);

        memberRepository.save(member);
    }

    /**
     * 비밀번호 일치 시 계정 삭제
     * 계정이 가지고 있던 포스트 작성자명 전부 수정 (알수없음)
     */
    @Override
    public void signOut(MemberDefaultDto defaultDto) {
        if (!defaultDto.password().equals(defaultDto.cPassword())) {
            throw new MemberException(MemberExceptionType.PASSWORD_NOT_EQUAL);
        }

        Member member = memberRepository.findByUsername(defaultDto.username()).get();
        member.getPosts().forEach(i -> i.setCreatedBy("알 수 없음"));
        for (Post post : member.getPosts()) {
            post.setMember(null);
        }

        memberRepository.delete(member);
    }

    @Override
    public void update(MemberDefaultDto defaultDto) {
        if (defaultDto.invalidPassword()) return;

        Member member = memberRepository.findByUsername(defaultDto.username()).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)
        );

        member.updateNickname(defaultDto.nickname());
        member.setLastModifiedAt(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMember(String email) {
        return memberRepository.findByUsername(email).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)
        );
    }
}
