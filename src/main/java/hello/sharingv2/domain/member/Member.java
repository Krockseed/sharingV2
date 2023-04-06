package hello.sharingv2.domain.member;

import hello.sharingv2.domain.BaseTimeEntity;
import hello.sharingv2.domain.comment.Comment;
import hello.sharingv2.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
//@ToString
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; //email

    @Column(nullable = false)
    private String password; //password

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role authority;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Post> posts = new ArrayList<>();

    public void deletePost(Post post) {
        posts.remove(post);
        post.setMember(null);
    }

    @OneToMany(mappedBy = "writer", cascade = CascadeType.PERSIST)
    private List<Comment> comments = new ArrayList<>();

    private String refreshToken;

    /* attribute update */
    public void updatePassword(PasswordEncoder encoder, String rawPassword) {
        this.password = encoder.encode(rawPassword);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    /* 비밀번호 암호화 */
    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }
}
