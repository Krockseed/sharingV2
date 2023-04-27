package hello.sharingv2.domain.post.service;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import hello.sharingv2.domain.member.service.MemberService;
import hello.sharingv2.domain.post.Post;
import hello.sharingv2.domain.post.Tag;
import hello.sharingv2.domain.post.dto.PostDto;
import hello.sharingv2.domain.post.exception.PostException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class TagServiceTest {

    @Autowired TagService tagService;
    @Autowired MemberService memberService;
    @Autowired PostService postService;

    MemberSignUpDto signUpDto = new MemberSignUpDto("ww@mail", "1234", "seed", "Kim");
    PostDto postDto = new PostDto("hello", "spring", "kakao");
    PostDto postDto2 = new PostDto("hello2", "oop", "kakao");

    @BeforeEach
    void before() {
        memberService.signUp(signUpDto);
        Member member = memberService.getMember("ww@mail");

        postService.save(member.getId(), postDto);
        postService.save(member.getId(), postDto2);
    }

    @Test
    void insert_tag_success() {
        //given
        List<Post> posts = memberService.getMember("ww@mail").getPosts();
        Post post = posts.get(0);
        Tag tag = Tag.builder().description("java").build();
        Tag tag2 = Tag.builder().description("spring").build();
        Tag tag3 = Tag.builder().description("spring").build();

        //when
        tagService.save(tag, post.getId());
        tagService.save(tag2, post.getId());
        tagService.save(tag3, post.getId());

        //then
        assertEquals("java", post.getTags().get(0).getDescription());
        assertEquals(2, post.getTags().size());
    }

    @Test
    void insert_tagList_success() {
        //given
        List<Post> posts = memberService.getMember("ww@mail").getPosts();
        Post post = posts.get(0);
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(Tag.builder().description("java").build());
        tags.add(Tag.builder().description("c++").build());
        tags.add(Tag.builder().description("python").build());
        tags.add(Tag.builder().description("go").build());

        //when
        tagService.save(tags, post.getId());

        //then
        assertEquals(4, post.getTags().size());
        assertEquals(1L, post.getTags().stream().filter(i -> i.getDescription().equals("go")).count());
    }

    @Test
    void delete_success() {
        //given
        List<Post> posts = memberService.getMember("ww@mail").getPosts();
        Post post = posts.get(0);
        Tag tag = Tag.builder().description("java").build();
        Tag tag2 = Tag.builder().description("spring").build();
        Tag tag3 = Tag.builder().description("spring").build();

        //when
        tagService.save(tag, post.getId());
        tagService.save(tag2, post.getId());
        tagService.save(tag3, post.getId()); // spring 존재하므로 무시됨

        tagService.sync(tag2.getDescription(), post.getId()); // spring 삭제
        //then
        assertEquals(1, post.getTags().size());

        //Transaction silently rolled back because it has been marked as rollback-only 오류 발생
        //Unchecked error (Runtime error) 발생 시 rollback mark 가 찍히고 롤백되야 하는데 Rollback = false 로 인해 롤백불가
        //해결방법 : TagService getTag 의 Transaction 내부 NoRollbackFor = PostException.class 로 RunetimeException 을
        //상속받았지만 특별히 rollback-only 가 check 되지 않도록 한다
        assertThrows(PostException.class, () -> tagService.getTag(tag2.getDescription(), post.getId()));
    }
}