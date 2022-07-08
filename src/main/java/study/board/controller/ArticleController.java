package study.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.board.domain.Article;
import study.board.domain.Member;
import study.board.domain.MemberAuth;
import study.board.dto.ArticleDTO;
import study.board.service.ArticleService;
import study.board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static study.board.SessionConst.*;

@Slf4j
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    @GetMapping("/{articleId}")
    public String readArticle(@PathVariable("articleId") Long articleId,
                              Model model) {
        Optional<Article> article = articleService.findById(articleId);
        if(article.isEmpty()) {
            log.info("찾을 수 없는 페이지입니다.");
            return "redirect:/articles";
        }
        log.info("article.get().getViews() = {}", article.get().getViews());
        articleService.addViews(articleId);
        log.info("article.get().getViews() = {}", article.get().getViews());
        model.addAttribute("articleDTO", new ArticleDTO(article.get()));
        return "articles/readArticle";
    }

    @GetMapping("/new")
    public String createArticleForm(@ModelAttribute("articleDTO") ArticleDTO articleDTO) {
        return "articles/newArticle";
    }

    @PostMapping("/new")
    public String createArticle(@Valid @ModelAttribute("articleDTO") ArticleDTO articleDTO,
                                HttpServletRequest request) {
        if(request.getSession(false) == null) {
            log.info("세션 정보 없음");
            request.getSession();
            return "redirect:/";
        }
        String loginId = (String) request.getSession(false).getAttribute(LOGIN_MEMBER);
        if(loginId == null) {
            log.info("세션 정보 없음");
            request.getSession(false).invalidate();
            return "redirect:/";
        }

        Optional<Member> member = memberService.findByLoginId(loginId);
        if(member.isEmpty()) {
            log.info("세션 정보에 사용자 정보 없음");
            request.getSession(false).invalidate();
            return "redirect:/";
        }

        log.info("Article 생성 가능");
        Article article = new Article(
                member.get(),
                articleDTO.getTitle(),
                articleDTO.getContent(),
                articleDTO.getImgUrl(),
                0L,
                0L);
        articleService.save(article);
        log.info("saved Article {}", article);
        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(@PathVariable("articleId") Long articleId,
                                  Model model, HttpServletRequest request) {
        Optional<Article> article = articleService.findById(articleId);
        if(article.isEmpty()) {
            log.info("찾을 수 없는 페이지");
            return "redirect:/articles";
        }

        String loginId = (String) request.getSession(false).getAttribute(LOGIN_MEMBER);
        if(loginId == null) {
            log.info("세션 정보 없음");
            request.getSession(false).invalidate();
            return "redirect:/articles";
        }

        Optional<Member> member = memberService.findByLoginId(loginId);
        if(member.isEmpty()) {
            log.info("올바르지 않은 멤버가 세션에 저장됨");
            request.getSession(false).invalidate();
            return "redirect:/articles";
        }

        model.addAttribute("articleDTO", new ArticleDTO(article.get()));
        return "articles/editArticle";
    }

    @PostMapping("/{articleId}/edit")
    public String editArticleForm(@PathVariable("articleId") Long articleId,
                                  @Valid @ModelAttribute("articleDTO") ArticleDTO articleDTO,
                                  HttpServletRequest request) {

        Optional<Article> article = articleService.findById(articleId);
        if(article.isEmpty()) {
            log.info("찾을 수 없는 페이지");
            return "redirect:/articles";
        }

        String loginId = (String) request.getSession(false).getAttribute(LOGIN_MEMBER);
        if(loginId == null) {
            log.info("세션 정보 없음");
            request.getSession(false).invalidate();
            return "redirect:/articles";
        }

        Optional<Member> member = memberService.findByLoginId(loginId);
        if(member.isEmpty()) {
            log.info("올바르지 않은 멤버가 세션에 저장됨");
            request.getSession(false).invalidate();
            return "redirect:/articles";
        }

        if(!article.get().getCreatedBy().equals(loginId) ||
                member.get().getMemberAuth() != MemberAuth.ADMINISTRATOR ||
                member.get().getMemberAuth() != MemberAuth.MANAGER) {
            log.info("수정 요청 사용자의 권한 없음");
            return "redirect:/articles";
        }

        article.get().changeTitle(articleDTO.getTitle());
        article.get().changeContent(articleDTO.getContent());
        article.get().changeImgUrl(articleDTO.getImgUrl());
        article.get().changeLastModifiedDate(LocalDateTime.now());
        article.get().changeLastModifiedBy(member.get().getLoginId());

        return "articles/editArticle";
    }
}
