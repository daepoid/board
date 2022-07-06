package study.board.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    @NotBlank
    private String content;

    @Lob
    private String imgUrl;

    @Builder
    public Article(Member member, String content, String imgUrl) {
        this.member = member;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
