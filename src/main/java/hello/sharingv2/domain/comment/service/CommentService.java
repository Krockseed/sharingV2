package hello.sharingv2.domain.comment.service;

import hello.sharingv2.domain.comment.Comment;
import hello.sharingv2.domain.comment.dto.CommentDto;

import java.util.List;

/**
 * 댓글 구현 구조 {@/reply} : POST
 * 기능
 * save 와 saveRe 의 member_id 는 jwt 이기 때문에 필요,
 * session 사용 시 ApplicationContextHolder 에서 꺼내와 사용
 * 1. 댓글 저장 {@/reply/[post_id]}
 * 1.1 대댓글 저장 {@/reply/[post_id]/pid=[parent_id]}
 * 2. 댓글 삭제
 *  * 모든 대댓글은 댓글이 달림 (대댓글의 대댓글 X)
 *  * 대댓글이 달린 댓글 삭제 시 -> 내용만 변경(삭제된 댓글)
 *  * 대댓글이 없는 댓글 삭제 시 -> 전부 삭제 (디비 포함)
 *  * 대댓글 삭제 시 -> 전부 삭제 (디비 포함)
 * 3. 댓글 수정
 */
public interface CommentService {

    Long save(CommentDto commentDto, Long memberId, Long postId);

    Long saveRe(CommentDto commentDto, Long memberId, Long postId, Long parentId);

    void delete(Long id) throws Exception;

    void update(Long id, CommentDto commentDto) throws Exception;

    List<Comment> getComments(Long postId);
}