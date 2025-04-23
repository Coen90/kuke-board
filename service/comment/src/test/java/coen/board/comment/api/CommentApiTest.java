package coen.board.comment.api;

import coen.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest(1L, "my comment1", null, 1L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment1", response1.getParentCommentId(), 1L));
        CommentResponse response3 = createComment(new CommentCreateRequest(1L, "my comment1", response1.getParentCommentId(), 1L));

        System.out.println("commentId=%s".formatted(response1.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response2.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response3.getCommentId()));

//        commentId=173551819588739072
    //        commentId=173552955880218624
    //        commentId=173551820079472640
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", 173551819588739072L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("/v1/comments/{commentId}", 173551820079472640L)
                .retrieve()
                .body(Void.class);
    }

    private CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentId;
        private Long writerId;
    }
}
