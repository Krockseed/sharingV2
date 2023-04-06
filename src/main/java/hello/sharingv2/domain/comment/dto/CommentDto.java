package hello.sharingv2.domain.comment.dto;

import hello.sharingv2.domain.comment.Comment;
import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class CommentDto {

    private String content;

    private Member writer;

    private Post post;

    public CommentDto(String content) {
        this.content = content;
    }

    public Comment toEntity(Member member, Post post) {
        Comment comment = Comment.builder().content(content).replies(new ArrayList<>()).build();

        comment.addWriter(member);
        comment.addPost(post);

        return comment;
    }

    public Comment toEntity(Member member, Post post, Comment parent) {
        Comment comment = Comment.builder().content(content).replies(new ArrayList<>()).build();

        comment.addWriter(member);
        comment.addPost(post);
        comment.addParent(parent);

        return comment;
    }
}
