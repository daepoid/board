package study.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.board.dto.ArticleDTO;
import study.board.service.ArticleService;

import static study.board.ArticleConst.*;

@Slf4j
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleListController {

    private final ArticleService articleService;

    @GetMapping("")
    public String articleListV2(Model model, Pageable pageable) {
        Page<ArticleDTO> articleDTOs = articleService.findArticleDTOs(pageable);
        model.addAttribute("articleDTOs", articleDTOs);

        //        double start = Math.floor(memberDTOs.getNumber() / 5) * 5 + 1;
        double start = (int)(articleDTOs.getNumber() / 5) * 5 + 1;
        double last = start + 4 < articleDTOs.getTotalPages() ? start + 4 : Math.max(start, articleDTOs.getTotalPages());
        model.addAttribute("start", start);
        model.addAttribute("last", last);
        return "articles/articleList";
    }

    @GetMapping("/best")
    public String bestViewsArticlesV2(Model model, Pageable pageable) {
        Page<ArticleDTO> articleDTOs = articleService.findRecommendedArticleDTOs(thresholdStars, pageable);
        model.addAttribute("articleDTOs", articleDTOs);

        //        double start = Math.floor(memberDTOs.getNumber() / 5) * 5 + 1;
        double start = (int)(articleDTOs.getNumber() / 5) * 5 + 1;
        double last = start + 4 < articleDTOs.getTotalPages() ? start + 4 : Math.max(start, articleDTOs.getTotalPages());
        model.addAttribute("start", start);
        model.addAttribute("last", last);
        return "articles/bestArticles";
    }

    @GetMapping("/monthly")
    public String monthlyPopularArticlesV2(Model model, Pageable pageable) {
        Page<ArticleDTO> articleDTOs = articleService.findPopularArticlesOrderByDays(31, thresholdViews, pageable);
        model.addAttribute("articleDTOs", articleDTOs);

        //        double start = Math.floor(memberDTOs.getNumber() / 5) * 5 + 1;
        double start = (int)(articleDTOs.getNumber() / 5) * 5 + 1;
        double last = start + 4 < articleDTOs.getTotalPages() ? start + 4 : Math.max(start, articleDTOs.getTotalPages());
        model.addAttribute("start", start);
        model.addAttribute("last", last);
        return "articles/monthlyArticles";
    }

    @GetMapping("/weekly")
    public String weeklyPopularArticlesV2(Model model, Pageable pageable) {
        Page<ArticleDTO> articleDTOs = articleService.findPopularArticlesOrderByDays(7, thresholdViews, pageable);
        model.addAttribute("articleDTOs", articleDTOs);

        //        double start = Math.floor(memberDTOs.getNumber() / 5) * 5 + 1;
        double start = (int)(articleDTOs.getNumber() / 5) * 5 + 1;
        double last = start + 4 < articleDTOs.getTotalPages() ? start + 4 : Math.max(start, articleDTOs.getTotalPages());
        model.addAttribute("start", start);
        model.addAttribute("last", last);
        return "articles/weeklyArticles";
    }

    @GetMapping("/newly")
    public String newlyViewsArticlesV2(Model model, Pageable pageable) {
        Page<ArticleDTO> articleDTOs = articleService.findArticleDTOsRecentNDays(7, pageable);
        model.addAttribute("articleDTOs", articleDTOs);

        //        double start = Math.floor(memberDTOs.getNumber() / 5) * 5 + 1;
        double start = (int)(articleDTOs.getNumber() / 5) * 5 + 1;
        double last = start + 4 < articleDTOs.getTotalPages() ? start + 4 : Math.max(start, articleDTOs.getTotalPages());
        model.addAttribute("start", start);
        model.addAttribute("last", last);
        return "articles/newlyArticles";
    }
}
