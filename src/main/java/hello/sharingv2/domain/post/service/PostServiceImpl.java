package hello.sharingv2.domain.post.service;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.exception.MemberException;
import hello.sharingv2.domain.member.exception.MemberExceptionType;
import hello.sharingv2.domain.member.repository.MemberRepository;
import hello.sharingv2.domain.post.Post;
import hello.sharingv2.domain.post.dto.PostDto;
import hello.sharingv2.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    public void save(Long memberId, PostDto postDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)
        );

        log.info("member = {}", member);
        Post post = postDto.toEntity();
        post.setCreatedBy(member.getNickname());
        post.addMember(member);

        postRepository.save(post);
    }

    @Override
    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchElementException::new);

        Member member = post.getMember();
        member.deletePost(post);

        postRepository.delete(post);
    }

    @Override
    public void update(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchElementException::new);

        // dirty check
        post.updateTitle(postDto.title());
        post.updateContent(postDto.content());
        post.updateContactWay(postDto.contactWay());
        post.setLastModifiedAt(LocalDateTime.now());
    }

    public int updateHits(Long postId) {
        return postRepository.updateHits(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return postRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return new ArrayList<>(postRepository.getAllBy());
    }

}
