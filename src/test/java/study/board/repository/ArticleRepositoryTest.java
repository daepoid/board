package study.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.Article;
import study.board.domain.Member;
import study.board.domain.MemberAuth;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static study.board.domain.MemberAuth.*;

@SpringBootTest
@Transactional
class ArticleRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Member createMember(String username, String loginId, MemberAuth memberAuth) {
        Member member = Member.builder()
                .username(username)
                .loginId(loginId)
                .password(passwordEncoder.encode("123"))
                .memberAuth(memberAuth)
                .createdDate(LocalDateTime.now())
                .build();
        em.persist(member);
        return member;
    }

    @Test
    public void BasicCRUDNormalCase() throws Exception {
        Member member = createMember("username", "loginId", NORMAL);

        String title = "title";
        String content = "content";
        String imgUrl = "imgUrl";
        Article article = Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .build();

        // Create
        articleRepository.save(article);
        em.flush();
        em.clear();

        // Read
        Optional<Article> findArticle = articleRepository.findById(article.getId());
        assertThat(findArticle.orElse(null)).isNotNull();
        assertThat(findArticle.get().getId()).isEqualTo(article.getId());
        assertThat(findArticle.get().getTitle()).isEqualTo(article.getTitle());

        // update
        String newTitle = "newTitle";
        String newContent = "newContent";
        String newImgUrl = "newImgUrl";
        findArticle.get().changeTitle(newTitle);
        findArticle.get().changeContent(newContent);
        findArticle.get().changeImgUrl(newImgUrl);
        assertThat(findArticle.get().getTitle()).isEqualTo(newTitle);
        assertThat(findArticle.get().getContent()).isEqualTo(newContent);
        assertThat(findArticle.get().getImgUrl()).isEqualTo(newImgUrl);

        em.flush();
        em.clear();
        Optional<Article> updatedArticle = articleRepository.findById(findArticle.get().getId());
        assertThat(updatedArticle.orElse(null)).isNotNull();
        assertThat(updatedArticle.get().getTitle()).isEqualTo(newTitle);
        assertThat(updatedArticle.get().getContent()).isEqualTo(newContent);
        assertThat(updatedArticle.get().getImgUrl()).isEqualTo(newImgUrl);

        // delete
        articleRepository.delete(updatedArticle.get());
        Optional<Article> deletedArticle = articleRepository.findById(updatedArticle.get().getId());
        assertThat(deletedArticle.orElse(null)).isNull();
    }
}