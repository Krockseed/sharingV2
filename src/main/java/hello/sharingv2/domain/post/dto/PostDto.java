package hello.sharingv2.domain.post.dto;

import hello.sharingv2.domain.post.Post;

import java.util.ArrayList;

public record PostDto(String title, String content, String contactWay) {

    public Post toEntity() {
        return Post.builder().title(title).content(content)
                .contactWay(contactWay).comments(new ArrayList<>())
                .tags(new ArrayList<>()).build();
    }
}
