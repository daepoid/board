package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.Article;
import study.board.dto.ArticleDTO;
import study.board.dto.ArticleUpdatableDTO;
import study.board.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Long save(Article article) {
        articleRepository.save(article);
        return article.getId();
    }

    public Optional<Article> findById(Long articleId) {
        return articleRepository.findById(articleId);
    }

    public Optional<ArticleDTO> findArticleDTO(Long articleId) {
        return articleRepository.findArticleDTO(articleId);
    }

    public List<ArticleDTO> findArticleDTOs() {
        return articleRepository.findArticleDTOs();
    }

    public Page<ArticleDTO> findArticleDTOs(Pageable pageable) {
        return articleRepository.findArticleDTOs(pageable);
    }

    public List<ArticleDTO> findPopularArticlesOrderByDays() {
        return articleRepository.findArticleDTOsOrderByViews();
    }

    public List<ArticleDTO> findPopularArticlesOrderByDays(long days, long views) {
        return articleRepository.findArticleDTOsOrderByViews(days, views);
    }

    public Page<ArticleDTO> findPopularArticlesOrderByDays(long days, long views, Pageable pageable) {
        return articleRepository.findArticleDTOsOrderByViews(days, views, pageable);
    }

    public List<ArticleDTO> findRecommendedArticleDTOs(long threshold) {
        return articleRepository.findArticleDTOsOrderByStarsGreaterThan(threshold);
    }

    public Page<ArticleDTO> findRecommendedArticleDTOs(long threshold, Pageable pageable) {
        return articleRepository.findArticleDTOsOrderByStarsGreaterThan(threshold, pageable);
    }

    public List<ArticleDTO> findArticleDTOsRecentNDays(long days) {
        return articleRepository.findArticleDTOsRecentNDays(days);
    }

    public Page<ArticleDTO> findArticleDTOsRecentNDays(long days, Pageable pageable) {
        return articleRepository.findArticleDTOsRecentNDays(days, pageable);
    }

    @Transactional
    public void addViews(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        article.ifPresent(Article::addViews);
    }

    @Transactional
    public void updateArticle(Long articleId, ArticleUpdatableDTO articleUpdatableDTO) {
        Optional<Article> article = articleRepository.findById(articleId);
        if(article.isEmpty()) {
            return;
        }

        String newTitle = articleUpdatableDTO.getTitle();
        String newContent = articleUpdatableDTO.getContent();
        String newImgUrl = articleUpdatableDTO.getImgUrl();

        article.get().changeTitle(newTitle.isBlank() ? article.get().getTitle() : newTitle);
        article.get().changeTitle(newContent.isBlank() ? article.get().getTitle() : newContent);
        article.get().changeTitle(newImgUrl.isBlank() ? article.get().getTitle() : newImgUrl);
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        article.ifPresent(articleRepository::delete);
    }
}
