package hello.sharingv2.domain.comment.repository;

import hello.sharingv2.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
