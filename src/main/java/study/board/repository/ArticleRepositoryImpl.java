package study.board.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import study.board.dto.ArticleDTO;
import study.board.dto.QArticleDTO;

import java.time.LocalDateTime;
import java.util.List;

import static study.board.domain.QArticle.*;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ArticleDTO> findArticleDTOs() {
        return queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .fetch();
    }

    @Override
    public Page<ArticleDTO> findArticleDTOs(Pageable pageable) {
        List<ArticleDTO> content = queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ArticleDTO> findAllOrderByCreatedDate(Pageable pageable) {
        List<ArticleDTO> content = queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .orderBy(article.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<ArticleDTO> findArticleDTOsOrderByViews() {
        return queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .orderBy(article.views.desc())
                .fetch();
    }

    @Override
    public Page<ArticleDTO> findArticleDTOsOrderByViews(Pageable pageable) {
        List<ArticleDTO> content = queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .orderBy(article.views.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<ArticleDTO> findArticleDTOsOrderByViews(long days, long views) {
        LocalDateTime now = LocalDateTime.now();
        return queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .orderBy(article.views.desc())
                .where(
                        article.createdDate.after(now.minusDays(days)),
                        article.views.goe(views))
                .fetch();
    }

    @Override
    public Page<ArticleDTO> findArticleDTOsOrderByViews(long days, long views, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        List<ArticleDTO> content = queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .orderBy(article.views.desc())
                .where(
                        article.createdDate.after(now.minusDays(days)),
                        article.views.goe(views))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .where(article.createdDate.after(now.minusDays(days)))
                .from(article);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<ArticleDTO> findArticleDTOsOrderByStarsGreaterThan(long threshold) {
        return queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .orderBy(article.stars.desc())
                .where(article.stars.goe(threshold))
                .fetch();
    }

    @Override
    public Page<ArticleDTO> findArticleDTOsOrderByStarsGreaterThan(long threshold, Pageable pageable) {
        List<ArticleDTO> content = queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .orderBy(article.stars.desc())
                .where(article.stars.goe(threshold))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<ArticleDTO> findArticleDTOsRecentNDays(long days) {
        LocalDateTime now = LocalDateTime.now();
        return queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .where(article.createdDate.after(now.minusDays(days)))
                .fetch();
    }

    @Override
    public Page<ArticleDTO> findArticleDTOsRecentNDays(long days, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        List<ArticleDTO> content = queryFactory
                .select(new QArticleDTO(article))
                .from(article)
                .where(article.createdDate.after(now.minusDays(days)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
