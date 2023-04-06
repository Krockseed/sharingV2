package hello.sharingv2.domain.post.service;

import hello.sharingv2.domain.post.Post;
import hello.sharingv2.domain.post.dto.PostDto;

import java.util.List;

public interface PostService {

    /* save 반환 시 Long 으로 Entity 의 Id 를 반환하면
     TestCase 작성 시 이점이 많은데 CQS 에 위배됨.. */
    void save(Long memberId, PostDto postDto);

    void delete(Long postId);

    void update(Long postId, PostDto postDto);

    Post getPost(Long id);

    List<Post> getAllPosts();
}
