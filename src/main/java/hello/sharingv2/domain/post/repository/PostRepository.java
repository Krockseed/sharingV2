package hello.sharingv2.domain.post.repository;

import hello.sharingv2.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> getAllBy();

    @Modifying
    @Query("update Post p set p.hits = p.hits + 1 where p.id = :postId")
    int updateHits(Long postId);
}
