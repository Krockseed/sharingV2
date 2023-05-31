package hello.sharingv2.domain.member;

import hello.sharingv2.domain.member.dto.MemberDefaultDto;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import hello.sharingv2.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("test/v1/signup")
    public ResponseEntity signUp(@RequestBody MemberSignUpDto signUpDto) {
        memberService.signUp(signUpDto);

        log.info("member signUp Success");
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/test/v1/{email}")
    public ResponseEntity getMemberInfo(@PathVariable String email) {
        Member member = memberService.getMember(email);
        log.info("this");

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/m/{member_id}")
    public ResponseEntity<String> updateMember(@ModelAttribute MemberDefaultDto memberDto) {
        memberService.update(memberDto);
        return new ResponseEntity<>("true", HttpStatus.OK);
    }

    @DeleteMapping("/m/{member_id}")
    public ResponseEntity<String> deleteMember(@ModelAttribute MemberDefaultDto memberDto) {
        memberService.signOut(memberDto);
        return new ResponseEntity<>("true", HttpStatus.OK);
    }
}
