package hello.sharingv2.domain.comment.service;

import hello.sharingv2.domain.comment.Comment;
import hello.sharingv2.domain.comment.dto.CommentDto;
import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import hello.sharingv2.domain.member.service.MemberService;
import hello.sharingv2.domain.post.Post;
import hello.sharingv2.domain.post.dto.PostDto;
import hello.sharingv2.domain.post.repository.PostRepository;
import hello.sharingv2.domain.post.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 댓글 테스트. {@/reply/[post_id]} : POST
 * 삽입.
 *
 *
 * 삭제.
 * 모든 대댓글은 댓글이 달림 (대댓글의 대댓글 X)
 * 대댓글이 달린 댓글 삭제 시 -> 내용만 변경(삭제된 댓글)
 * 대댓글이 없는 댓글 삭제 시 -> 전부 삭제 (디비 포함)
 * 대댓글 삭제 시 -> 전부 삭제 (디비 포함)
 */
@SpringBootTest
@Transactional
class CommentServiceImplTest {

    @Autowired CommentService commentService;
    @Autowired PostService postService;
    @Autowired MemberService memberService;

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
    void comment_save_success() {
        Member member = memberService.getMember("ww@mail");
        Post post = postService.getPost(member.getPosts().get(0).getId());
        CommentDto commentDto = new CommentDto("hi"); // 댓글

        commentService.save(commentDto, member.getId(), post.getId());
        List<Comment> comments = commentService.getComments(post.getId());

        Assertions.assertEquals("hi", comments.get(0).getContent());
    }

    @Test
    void reply_save_success() throws Exception {
        //given
        Member member = memberService.getMember("ww@mail");
        Post post = postService.getPost(member.getPosts().get(0).getId());
        CommentDto commentDto = new CommentDto("hi");
        CommentDto reply = new CommentDto("hi of hi");
        CommentDto reply2 = new CommentDto("hi of hi of hi");

        //when
        Long parentId = commentService.save(commentDto, member.getId(), post.getId());
        commentService.saveRe(reply, member.getId(), post.getId(), parentId);
        commentService.saveRe(reply2, member.getId(), post.getId(), parentId);

        //then
        List<Comment> comments = commentService.getComments(post.getId());
        Comment comment = comments.get(0);

        assertEquals(reply.getContent(), comment.getReplies().get(0).getContent());
        assertEquals(2, comment.getReplies().size());
    }

    @Test
    void 대댓글있는_댓글_삭제() throws Exception {
        Member member = memberService.getMember("ww@mail");
        Post post = postService.getPost(member.getPosts().get(0).getId());
        CommentDto commentDto = new CommentDto("hi"); // 댓글
        CommentDto commentDto2 = new CommentDto("spring"); // 댓글
        CommentDto commentDto3 = new CommentDto("starbucks"); // 댓글


    }

    @Test
    void 대댓글_삭제() throws Exception {
        Member member = memberService.getMember("ww@mail");
        Post post = postService.getPost(member.getPosts().get(0).getId());
        CommentDto commentDto = new CommentDto("hi"); // 댓글
        CommentDto commentDto2 = new CommentDto("spring"); // 댓글
        CommentDto commentDto3 = new CommentDto("starbucks"); // 댓글

        commentService.save(commentDto, member.getId(), post.getId());
        commentService.save(commentDto2, member.getId(), post.getId());
        commentService.save(commentDto3, member.getId(), post.getId());

        List<Comment> comments = commentService.getComments(post.getId());
        commentService.delete(comments.get(0).getId());
    }

    @Test
    void update() {
    }

}