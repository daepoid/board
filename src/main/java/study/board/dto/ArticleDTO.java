package study.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import lombok.ToString;
import study.board.domain.Article;
import study.board.domain.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ArticleDTO {

    private Long id;

    private Member member;

    private String title;

    private String content;

    private String imgUrl;

    //==추가 정보==//
    private Long stars = 0L;

    private Long views = 0L;

    private String createdBy;

    private String lastModifiedBy;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    @QueryProjection
    public ArticleDTO(Long id, Member member, String title, String content,
                      String imgUrl, Long stars, Long views,
                      String createdBy, String lastModifiedBy,
                      LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.member = member;

        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;

        this.stars = stars;
        this.views = views;

        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    @QueryProjection
    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.member = article.getMember() == null ? null : article.getMember();

        this.title = article.getTitle();
        this.content = article.getContent();
        this.imgUrl = article.getImgUrl();

        this.stars = article.getStars();
        this.views = article.getViews();

        this.createdBy = article.getCreatedBy();
        this.lastModifiedBy = article.getLastModifiedBy();
        this.createdDate = article.getCreatedDate();
        this.lastModifiedDate = article.getLastModifiedDate();
    }
}
