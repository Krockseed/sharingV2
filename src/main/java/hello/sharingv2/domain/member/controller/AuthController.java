package hello.sharingv2.domain.member.controller;

import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import hello.sharingv2.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody MemberSignUpDto signUpDto) {
        log.info("dto = {}", signUpDto);

        memberService.signUp(signUpDto);

        return ResponseEntity.ok().build();
    }
}
