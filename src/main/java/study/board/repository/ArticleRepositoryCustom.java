package study.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.board.dto.ArticleDTO;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<ArticleDTO> findArticleDTOs();
    Page<ArticleDTO> findArticleDTOs(Pageable pageable);
    Page<ArticleDTO> findAllOrderByCreatedDate(Pageable pageable);
    List<ArticleDTO> findArticleDTOsOrderByViews();
    Page<ArticleDTO> findArticleDTOsOrderByViews(Pageable pageable);
    List<ArticleDTO> findArticleDTOsOrderByViews(long days, long views);
    Page<ArticleDTO> findArticleDTOsOrderByViews(long days, long views, Pageable pageable);
    List<ArticleDTO> findArticleDTOsOrderByStarsGreaterThan(long threshold);
    Page<ArticleDTO> findArticleDTOsOrderByStarsGreaterThan(long threshold, Pageable pageable);
    List<ArticleDTO> findArticleDTOsRecentNDays(long days);
    Page<ArticleDTO> findArticleDTOsRecentNDays(long days, Pageable pageable);
}
