package study.board.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.board.repository.ArticleRepository;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleRepository articleRepository;

}
