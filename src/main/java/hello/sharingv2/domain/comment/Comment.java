package hello.sharingv2.domain.comment;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Lob
    private String content;

    private String createdBy;

    private boolean isRemoved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> replies = new ArrayList<>();

    public void remove() {
        this.isRemoved = true;
        this.content = "삭제된 댓글입니다";
    }

    public void addWriter(Member member) {
        this.writer = member;
        writer.getComments().add(this);
    }

    public void addPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    public void addParent(Comment parent) {
        this.parent = parent;
        parent.getReplies().add(this);
    }

    public void updateComment(String content) {
        this.content = content;
    }

    public boolean isAllRepliesRemoved() {
        return replies.stream()
                .map(Comment::isRemoved)
                .filter(isRemoved -> !isRemoved)
                .findAny()
                .orElse(true);
    }
}
