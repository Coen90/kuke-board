package coen.board.article.controller;

import coen.board.article.service.ArticleService;
import coen.board.article.service.request.ArticleCreateRequest;
import coen.board.article.service.request.ArticleUpdateRequest;
import coen.board.article.service.response.ArticlePageResponse;
import coen.board.article.service.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/v1/articles/{articleId}")
    public ArticleResponse readArticle(@PathVariable Long articleId) {
        return articleService.read(articleId);
    }

    @GetMapping("/v1/articles")
    public ArticlePageResponse readAllArticles(
            @RequestParam Long boardId,
            @RequestParam Long page,
            @RequestParam Long pageSize
    ) {
        return articleService.readAll(boardId, page, pageSize);
    }

    @PostMapping("/v1/articles")
    public ArticleResponse createArticle(@RequestBody ArticleCreateRequest articleCreateRequest) {
        return articleService.create(articleCreateRequest);
    }

    @PutMapping("/v1/articles/{articleId}")
    public ArticleResponse updateArticle(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest articleUpdateRequest) {
        return articleService.update(articleId, articleUpdateRequest);
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public void deleteArticle(@PathVariable Long articleId) {
        articleService.delete(articleId);
    }
}
