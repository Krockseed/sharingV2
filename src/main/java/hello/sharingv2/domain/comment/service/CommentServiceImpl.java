package hello.sharingv2.domain.comment.service;

import hello.sharingv2.domain.comment.Comment;
import hello.sharingv2.domain.comment.dto.CommentDto;
import hello.sharingv2.domain.comment.repository.CommentRepository;
import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.exception.MemberException;
import hello.sharingv2.domain.member.exception.MemberExceptionType;
import hello.sharingv2.domain.member.repository.MemberRepository;
import hello.sharingv2.domain.post.Post;
import hello.sharingv2.domain.post.exception.PostException;
import hello.sharingv2.domain.post.exception.PostExceptionType;
import hello.sharingv2.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public Long save(CommentDto commentDto, Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_POST)
        );

        Comment comment = commentDto.toEntity(member, post);

        return commentRepository.save(comment).getId();
    }

    @Override
    public Long saveRe(CommentDto commentDto, Long memberId, Long postId, Long parentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_POST)
        );
        Comment parent = commentRepository.findById(parentId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_COMMENT)
        );

        Comment comment = commentDto.toEntity(member, post, parent);

        return commentRepository.save(comment).getId();
    }

    @Override
    public void delete(Long id) throws Exception {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_COMMENT)
        );

        comment.remove();

        if (comment.getParent() == null) {
            if (comment.isAllRepliesRemoved()) {
                commentRepository.delete(comment);
            }
        } else {
            commentRepository.delete(comment);
        }
    }

    @Override
    public void update(Long id, CommentDto commentDto) throws Exception {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_COMMENT)
        );

        comment.updateComment(commentDto.getContent());
    }

    @Override
    public List<Comment> getComments(Long postId) {
        return commentRepository.findAllById(Collections.singleton(postId));
    }
}
