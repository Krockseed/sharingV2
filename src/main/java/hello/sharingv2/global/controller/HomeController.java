package hello.sharingv2.global.controller;

import hello.sharingv2.domain.member.service.MemberService;
import hello.sharingv2.domain.post.service.PostService;
import hello.sharingv2.domain.post.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final PostService postService;
    private final TagService tagService;

    @GetMapping({"/", "", "/index"})
    public ResponseEntity index() {
        return new ResponseEntity("hello", HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity test(@RequestParam String title, @RequestParam String content) {
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
