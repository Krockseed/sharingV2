package hello.sharingv2.domain.comment.service;

import hello.sharingv2.domain.comment.Comment;
import hello.sharingv2.domain.comment.dto.CommentDto;
import hello.sharingv2.domain.comment.repository.CommentRepository;
import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.dto.MemberSignUpDto;
import hello.sharingv2.domain.member.service.MemberService;
import hello.sharingv2.domain.post.Post;
import hello.sharingv2.domain.post.dto.PostDto;
import hello.sharingv2.domain.post.exception.PostException;
import hello.sharingv2.domain.post.exception.PostExceptionType;
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
    @Autowired CommentRepository commentRepository;

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
        Comment comment = commentRepository.findById(parentId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_COMMENT)
        );

        assertEquals(reply.getContent(), comment.getReplies().get(0).getContent());
        assertEquals(2, comment.getReplies().size());
    }

    /**
     * 대댓글 있는 댓글 삭제 시 (알 수 없음) 변경 후 디비 삭제 x
     */
    @Test
    void 대댓글있는_댓글_삭제() throws Exception {
        Member member = memberService.getMember("ww@mail");
        Post post = postService.getPost(member.getPosts().get(0).getId());
        CommentDto commentDto = new CommentDto("hi"); // 댓글
        CommentDto reComment = new CommentDto("hi2");
        CommentDto reComment2 = new CommentDto("hi3");

        Long originId = commentService.save(commentDto, member.getId(), post.getId());
        commentService.saveRe(reComment2, member.getId(), post.getId(), originId);
        commentService.saveRe(reComment, member.getId(), post.getId(), originId);

        commentService.delete(originId);
        Comment comment = commentRepository.findById(originId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_COMMENT)
        );

        assertEquals("삭제된 댓글입니다", comment.getContent());
        assertEquals(2, comment.getReplies().size());
    }

    /*
        대댓글 없는 댓글 삭제 시 : 바로 삭제 (디비 삭제)
     */
    @Test
    void 대댓글_없는_댓글_삭제() throws Exception {
        Member member = memberService.getMember("ww@mail");
        Post post = postService.getPost(member.getPosts().get(0).getId());
        CommentDto commentDto = new CommentDto("hi"); // 댓글

        Long originId = commentService.save(commentDto, member.getId(), post.getId());

        commentService.delete(originId);

        assertThrows(PostException.class, () -> commentService.getComment(originId));
    }

    @Test
    void update_success() throws Exception {
        //given
        Member member = memberService.getMember("ww@mail");
        Post post = postService.getPost(member.getPosts().get(0).getId());
        CommentDto commentDto = new CommentDto("hi"); // 댓글
        CommentDto updateDto = new CommentDto("revised content");

        Long originId = commentService.save(commentDto, member.getId(), post.getId());
        //when
        Comment comment = commentRepository.findById(originId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_COMMENT)
        );

        commentService.update(comment.getId(), updateDto);

        //then
        assertEquals(updateDto.getContent(), comment.getContent());
    }
}