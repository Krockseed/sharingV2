package hello.sharingv2.domain.post.repository;

import hello.sharingv2.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> getAllBy();
}
