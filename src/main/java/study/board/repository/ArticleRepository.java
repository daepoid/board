package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
