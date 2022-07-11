package study.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.Article;
import study.board.domain.Member;
import study.board.domain.MemberAuth;
import study.board.dto.ArticleDTO;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static study.board.domain.MemberAuth.*;

@SpringBootTest
@Transactional
class ArticleRepositoryImplTest {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EntityManager em;

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


//    @BeforeEach
//    public void before() {
//        String username = "username";
//        String loginId = "loginId";
//        MemberAuth memberAuth = NORMAL;
//
//        Member member = createMember(username, loginId, memberAuth);
//        em.persist(member);
//    }

    @Test
    public void findArticleDTO() throws Exception {
        String username = "username";
        String loginId = "loginId";
        MemberAuth memberAuth = NORMAL;

        Member member = createMember(username, loginId, memberAuth);
        em.persist(member);

        String title = "title";
        String content = "content";
        String imgUrl = "imgUrl";
        Article article = Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .views(0L)
                .stars(0L)
                .build();
        em.persist(article);

        Optional<ArticleDTO> articleDTO = articleRepository.findArticleDTO(article.getId());
        assertThat(articleDTO.orElse(null)).isNotNull();
        System.out.println("articleDTO = " + articleDTO.get());
        assertThat(articleDTO.get().getTitle()).isEqualTo(title);
        assertThat(articleDTO.get().getContent()).isEqualTo(content);
        assertThat(articleDTO.get().getImgUrl()).isEqualTo(imgUrl);
        assertThat(articleDTO.get().getStars()).isEqualTo(0);
        assertThat(articleDTO.get().getViews()).isEqualTo(0);
    }

    @Test
    public void findArticleDTOsList() throws Exception {
        String username = "username";
        String loginId = "loginId";
        MemberAuth memberAuth = NORMAL;

        Member member = createMember(username, loginId, memberAuth);
        em.persist(member);

        String title = "title";
        String content = "content";
        String imgUrl = "imgUrl";
        Article article = Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .views(0L)
                .stars(0L)
                .build();
        em.persist(article);

        List<ArticleDTO> articleDTOs = articleRepository.findArticleDTOs();
        assertThat(articleDTOs.size()).isEqualTo(1);
        assertThat(articleDTOs.get(0).getTitle()).isEqualTo(title);
    }

    @Test
    public void findArticleDTOsPage() throws Exception {
        String username = "username";
        String loginId = "loginId";
        MemberAuth memberAuth = NORMAL;

        Member member = createMember(username, loginId, memberAuth);
        em.persist(member);

        String title = "title";
        String content = "content";
        String imgUrl = "imgUrl";
        Article article = Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .views(0L)
                .stars(0L)
                .build();
        em.persist(article);

        Pageable pageable = PageRequest.of(0, 5);

        Page<ArticleDTO> articleDTOs = articleRepository.findArticleDTOs(pageable);
        assertThat(articleDTOs.getTotalElements()).isEqualTo(1);
        assertThat(articleDTOs.getTotalPages()).isEqualTo(1);
        assertThat(articleDTOs.getNumber()).isEqualTo(0);
        assertThat(articleDTOs.getSize()).isEqualTo(5);
    }

    @Test
    public void findAllOrderByCreatedDatePage() throws Exception {
        String username = "username";
        String loginId = "loginId";
        MemberAuth memberAuth = NORMAL;

        Member member = createMember(username, loginId, memberAuth);
        em.persist(member);

        String title1 = "title1";
        String content1 = "content1";
        String imgUrl1 = "imgUrl1";
        Article article1 = Article.builder()
                .member(member)
                .title(title1)
                .content(content1)
                .imgUrl(imgUrl1)
                .views(0L)
                .stars(0L)
                .build();
        em.persist(article1);

        String title2 = "title2";
        String content2 = "content2";
        String imgUrl2 = "imgUrl2";
        Article article2 = Article.builder()
                .member(member)
                .title(title2)
                .content(content2)
                .imgUrl(imgUrl2)
                .views(0L)
                .stars(0L)
                .build();
        em.persist(article2);

        Page<ArticleDTO> articleDTOs = articleRepository.findAllOrderByCreatedDate(PageRequest.of(0, 5));
        assertThat(articleDTOs.getContent().get(0).getId()).isEqualTo(article2.getId());
    }

    @Test
    public void findArticleDTOsOrderByViewsPage() throws Exception {
        String username = "username";
        String loginId = "loginId";
        MemberAuth memberAuth = NORMAL;

        Member member = createMember(username, loginId, memberAuth);
        em.persist(member);

        String title1 = "title1";
        String content1 = "content1";
        String imgUrl1 = "imgUrl1";
        Article article1 = Article.builder()
                .member(member)
                .title(title1)
                .content(content1)
                .imgUrl(imgUrl1)
                .views(10L)
                .stars(10L)
                .build();
        em.persist(article1);

        String title2 = "title2";
        String content2 = "content2";
        String imgUrl2 = "imgUrl2";
        Article article2 = Article.builder()
                .member(member)
                .title(title2)
                .content(content2)
                .imgUrl(imgUrl2)
                .views(20L)
                .stars(20L)
                .build();
        em.persist(article2);

        Page<ArticleDTO> articleDTOs = articleRepository.findArticleDTOsOrderByViews(PageRequest.of(0, 5));
        assertThat(articleDTOs.getContent().get(0).getId()).isEqualTo(article2.getId());
    }

    @Test
    public void findArticleDTOsOrderByViewsWhereDatePage() throws Exception {
        String username = "username";
        String loginId = "loginId";
        MemberAuth memberAuth = NORMAL;

        Member member = createMember(username, loginId, memberAuth);
        em.persist(member);

        String title1 = "title1";
        String content1 = "content1";
        String imgUrl1 = "imgUrl1";
        Article article1 = Article.builder()
                .member(member)
                .title(title1)
                .content(content1)
                .imgUrl(imgUrl1)
                .views(10L)
                .stars(10L)
                .build();
        em.persist(article1);

        String title2 = "title2";
        String content2 = "content2";
        String imgUrl2 = "imgUrl2";
        Article article2 = Article.builder()
                .member(member)
                .title(title2)
                .content(content2)
                .imgUrl(imgUrl2)
                .views(20L)
                .stars(20L)
                .build();
        em.persist(article2);

        Page<ArticleDTO> articleDTOs = articleRepository.findArticleDTOsOrderByViews(1, 15, PageRequest.of(0, 5));
        assertThat(articleDTOs.getContent().get(0).getId()).isEqualTo(article2.getId());
    }

    @Test
    public void findArticleDTOsOrderByStarsGreaterThanPage() throws Exception {
        String username = "username";
        String loginId = "loginId";
        MemberAuth memberAuth = NORMAL;

        Member member = createMember(username, loginId, memberAuth);
        em.persist(member);

        String title1 = "title1";
        String content1 = "content1";
        String imgUrl1 = "imgUrl1";
        Article article1 = Article.builder()
                .member(member)
                .title(title1)
                .content(content1)
                .imgUrl(imgUrl1)
                .views(10L)
                .stars(10L)
                .build();
        em.persist(article1);

        String title2 = "title2";
        String content2 = "content2";
        String imgUrl2 = "imgUrl2";
        Article article2 = Article.builder()
                .member(member)
                .title(title2)
                .content(content2)
                .imgUrl(imgUrl2)
                .views(20L)
                .stars(20L)
                .build();
        em.persist(article2);

        Page<ArticleDTO> articleDTOs = articleRepository.findArticleDTOsOrderByStarsGreaterThan(15, PageRequest.of(0, 5));
        assertThat(articleDTOs.getContent().get(0).getId()).isEqualTo(article2.getId());
    }

    @Test
    public void findArticleDTOsRecentNDaysPage() throws Exception {
        String username = "username";
        String loginId = "loginId";
        MemberAuth memberAuth = NORMAL;

        Member member = createMember(username, loginId, memberAuth);
        em.persist(member);

        String title1 = "title1";
        String content1 = "content1";
        String imgUrl1 = "imgUrl1";
        Article article1 = Article.builder()
                .member(member)
                .title(title1)
                .content(content1)
                .imgUrl(imgUrl1)
                .views(10L)
                .stars(10L)
                .build();
        em.persist(article1);

        String title2 = "title2";
        String content2 = "content2";
        String imgUrl2 = "imgUrl2";
        Article article2 = Article.builder()
                .member(member)
                .title(title2)
                .content(content2)
                .imgUrl(imgUrl2)
                .views(20L)
                .stars(20L)
                .build();
        em.persist(article2);

        Page<ArticleDTO> articleDTOs = articleRepository.findArticleDTOsRecentNDays(3, PageRequest.of(0, 5));
        assertThat(articleDTOs.getContent().get(0).getId()).isEqualTo(article2.getId());
    }
}