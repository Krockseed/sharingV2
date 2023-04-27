package hello.sharingv2.domain.post;

import hello.sharingv2.domain.BaseTimeEntity;
import hello.sharingv2.domain.comment.Comment;
import hello.sharingv2.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    private String contactWay;

    private String createdBy;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int hits;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Comment> comments = new ArrayList<>();

    public void addMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
