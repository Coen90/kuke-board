package coen.board.article.api;

import coen.board.article.service.response.ArticlePageResponse;
import coen.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi", "my content", 1L, 1L
        ));
        System.out.println("response = " + response);
    }


    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(159781581441310720L);
        System.out.println("response = " + response);
    }

    ArticleResponse read(Long id) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", id)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void updateTest() {
        update(159781581441310720L, new ArticleUpdateRequest(
                "hi2", "my content2"
        ));
        ArticleResponse response = read(159781581441310720L);
        System.out.println("response = " + response);
    }

    void update(Long id, ArticleUpdateRequest request) {
        restClient.put()
                .uri("/v1/articles/{articleId}", id)
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void deleteTest() {
        delete(157985299572686848L);
        ArticleResponse response = read(157985299572686848L);
        System.out.println("response = " + response);
    }

    void delete(Long id) {
        restClient.delete()
                .uri("/v1/articles/{articleId}", id)
                .retrieve()
                .body(Void.class);
    }

    @Test
    void readAllTest() {
        ArticlePageResponse response = restClient.get()
                .uri("/v1/articles?boardId=1&page=50000&pageSize=30")
                .retrieve()
                .body(ArticlePageResponse.class);

        System.out.println("response = " + response.getArticleCount());
        for (coen.board.article.service.response.ArticleResponse article : response.getArticles()) {
            System.out.println("article = " + article);
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleResponse> response = restClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        System.out.println("firstPage");
        for (ArticleResponse articleResponse : response) {
            System.out.println("articleResponse.get = " + articleResponse.getArticleId());
        }

        List<ArticleResponse> response1 = restClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5&lastArticleId=%s".formatted(response.getLast().getArticleId()))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });
        System.out.println("secondPage");
        for (ArticleResponse articleResponse : response1) {
            System.out.println("articleResponse.get = " + articleResponse.getArticleId());
        }
    }

    @Getter
    @AllArgsConstructor
    static public class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static public class ArticleUpdateRequest {
        private String title;
        private String content;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    static public class ArticleResponse {
        private Long articleId;
        private String title;
        private String content;
        private Long boardId; // shard key
        private Long writerId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
