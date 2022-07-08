package study.board.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "title", "content", "imgUrl", "stars", "views", "createdBy", "lastModifiedBy", "createdDate", "lastModifiedDate"})
public class Article extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotBlank
    private String title;

    @Lob
    @NotBlank
    private String content;

    @Lob
    private String imgUrl;

    @PositiveOrZero
    private Long stars = 0L;

    @PositiveOrZero
    private Long views = 0L;

    @Builder
    public Article(Member member, String title, String content, String imgUrl, Long stars, Long views) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.stars = stars;
        this.views = views;

        this.changeCreatedBy(member.getLoginId());
        this.changeLastModifiedBy(member.getLoginId());

        LocalDateTime now = LocalDateTime.now();
        this.changeCreatedDate(now);
        this.changeLastModifiedDate(now);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void addStars() {
        log.info("Test addStars() = {}", this.stars);
        this.stars++;
        log.info("Test addStars() = {}", this.stars);
    }

    public void addViews() {
        log.info("Test addViews() = {}", this.views);
        this.views++;
        log.info("Test addViews() = {}", this.views);
    }
}
