package hello.sharingv2.domain;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.dto.MemberDefaultDto;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import hello.sharingv2.domain.member.repository.MemberRepository;
import hello.sharingv2.domain.member.service.MemberService;
import hello.sharingv2.domain.post.Post;
import hello.sharingv2.domain.post.dto.PostDto;
import hello.sharingv2.domain.post.repository.PostRepository;
import hello.sharingv2.domain.post.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * 1. 저장 성공 여부 (BaseTimeEntity 잘 들어갔는지)
 * 2. 삭제
 * 3. 수정
 * 4. 멤버 삭제 시, 기존 포스터 삭제 여부 -> 삭제 대신 작성자만 (알수없음) 변경
 */

@SpringBootTest
@Transactional
public class PostTest {

    @Autowired PostService postService;
    @Autowired MemberService memberService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 포스트_저장_성공() throws Exception {
        //given
        MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "1234", "seed", "Kim");
        PostDto postDto = new PostDto("hello", "spring", "kakao");
        PostDto postDto2 = new PostDto("hello2", "oop", "kakao");

        //when
        memberService.signUp(signUpDto);
        Member member = memberService.getMember("ww@mail");

        postService.save(member.getId(), postDto);
        postService.save(member.getId(), postDto2);

        Post post = postService.getPost(member.getPosts().get(0).getId());

        //then
        Assertions.assertEquals(2, member.getPosts().size());
        Assertions.assertEquals(member.getNickname(), post.getCreatedBy());
        Assertions.assertNotNull(post.getCreatedAt());
    }

    @Test
    void 포스트_삭제_성공() throws Exception {
        MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "1234", "seed", "Kim");
        PostDto postDto = new PostDto("hello", "spring", "kakao");
        PostDto postDto2 = new PostDto("hello2", "oop", "kakao");

        //when
        memberService.signUp(signUpDto);
        Member member = memberService.getMember("ww@mail");

        postService.save(member.getId(), postDto);
        postService.save(member.getId(), postDto2);

        //then
        postService.delete(member.getPosts().get(0).getId());

        Assertions.assertEquals(1, member.getPosts().size());
    }

    @Test
    void 포스트_수정_성공() throws Exception {
        MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "1234", "seed", "Kim");
        PostDto postDto = new PostDto("hello", "spring", "kakao");
        PostDto updateDto = new PostDto("bye", "oop", "kakao");

        //when
        memberService.signUp(signUpDto);
        Member member = memberService.getMember("ww@mail");

        postService.save(member.getId(), postDto);
        Post post = member.getPosts().get(0);

        //then
        postService.update(post.getId(), updateDto);
        Assertions.assertNotEquals(postDto.title(), post.getTitle());
    }

    @Test
    void 멤버삭제_포스터_작성자변경() throws Exception {
        MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "1234", "seed", "Kim");
        MemberDefaultDto defaultDto = new MemberDefaultDto("ww@mail", "1234", "1234", "seed");
        PostDto postDto = new PostDto("hello", "spring", "kakao");
        PostDto postDto2 = new PostDto("hello2", "oop", "kakao");

        //when
        memberService.signUp(signUpDto);
        Member member = memberService.getMember("ww@mail");

        postService.save(member.getId(), postDto);
        postService.save(member.getId(), postDto2);

        //then
        memberService.signOut(defaultDto);
        ArrayList<Post> posts = (ArrayList<Post>) postService.getAllPosts();

        for (Post post : posts) {
            Assertions.assertEquals("알 수 없음", post.getCreatedBy());
        }
    }
}
