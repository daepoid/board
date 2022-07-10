package study.board.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import study.board.domain.Article;
import study.board.domain.Member;
import study.board.dto.ArticleDTO;
import study.board.dto.ArticleUpdatableDTO;
import study.board.dto.CreateArticleDTO;
import study.board.service.ArticleService;
import study.board.service.MemberService;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@Api(tags = {"게시글 정보 API"})
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService articleService;
    private final MemberService memberService;

    @GetMapping("/v1/articles")
    @ApiOperation(value = "전체 게시글 페이징 조회", notes = "전체 게시글을 페이징 처리하여 페이지 타입으로 반환")
    public Page<ArticleDTO> findArticlesV1(Pageable pageable) {
        return articleService.findArticleDTOs(pageable);
    }

    @PostMapping("/v1/articles/new")
    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성하고 아이디를 반환")
    public Long createArticleV1(@RequestBody @Valid CreateArticleDTO createArticleDTO) {
        Optional<Member> member = memberService.findById(createArticleDTO.getMemberId());
        if(member.isEmpty()) {
            log.info("createArticleDTOV1 Error");
            return -1L;
        }

        Article article = Article.builder()
                .member(member.get())
                .title(createArticleDTO.getTitle())
                .content(createArticleDTO.getContent())
                .imgUrl(createArticleDTO.getImgUrl())
                .stars(0L)
                .views(0L)
                .build();

        return articleService.save(article);
    }

    @GetMapping("/v1/articles/{articleId}")
    @ApiOperation(value = "게시글 조회", notes = "게시글를 조회하여 게시글 정보를 반환")
    public ArticleDTO findArticleV1(@PathVariable("articleId") Long articleId) {
        return articleService.findArticleDTO(articleId).orElse(null);
    }

    @PostMapping("/v1/articles/{articleId}")
    @ApiOperation(value = "게시글 정보 수정", notes = "게시글 정보를 수정하고 아이디를 반환")
    public Long updateArticleV1(@PathVariable("articleId") Long articleId,
                                @RequestBody ArticleUpdatableDTO articleUpdatableDTO) {
        articleService.updateArticle(articleId, articleUpdatableDTO);
        return articleId;
    }

    @DeleteMapping("/v1/articles/{articleId}")
    @ApiOperation(value = "게시글 정보 삭제", notes = "게시글 정보를 삭제하고 아이디를 반환")
    public Long deleteArticleV1(@PathVariable("articleId") Long articleId) {
        articleService.deleteArticle(articleId);
        return articleId;
    }

}
