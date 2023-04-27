package hello.sharingv2.domain.post.service;

import hello.sharingv2.domain.post.Post;
import hello.sharingv2.domain.post.Tag;
import hello.sharingv2.domain.post.exception.PostException;
import hello.sharingv2.domain.post.exception.PostExceptionType;
import hello.sharingv2.domain.post.repository.PostRepository;
import hello.sharingv2.domain.post.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public void save(Tag tag, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_POST)
        );

        if (tag.getDescription().equals("")) return;
        if (tagRepository.existsByDescriptionAndPostId(tag.getDescription(), postId)) return;

        tag.addPost(post);
        tagRepository.save(tag);
    }

    public void save(List<Tag> tags, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_POST)
        );

        for (Tag tag : tags) {
            if (tag.getDescription().equals("")) continue;
            if (tagRepository.existsByDescriptionAndPostId(tag.getDescription(), postId)) continue;
            tag.addPost(post);
            tagRepository.save(tag);
        }
    }

    public void sync(String desc, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_POST)
        );
        Tag tag = tagRepository.findByDescriptionAndPostId(desc, postId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_TAG)
        );

        post.getTags().remove(tag);
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true, noRollbackFor = PostException.class)
    public Tag getTag(String desc, Long postId) {
        return tagRepository.findByDescriptionAndPostId(desc, postId).orElseThrow(
                () -> new PostException(PostExceptionType.NOT_FOUND_TAG)
        );
    }
}
