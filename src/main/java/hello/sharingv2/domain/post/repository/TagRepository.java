package hello.sharingv2.domain.post.repository;

import hello.sharingv2.domain.post.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByDescriptionAndPostId(String desc, Long postId);

    boolean existsByDescriptionAndPostId(String desc, Long postId);
}
