package coen.board.comment.api;

import coen.board.comment.service.response.CommentPageResponse;
import coen.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

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

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v1/comments?articleId={articleId}&page={page}&pageSize={pageSize}", 1L, 1L, 10L)
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
    }

    /**
     * comment.getCommentId() = 173198799236096000
     * 	comment.getCommentId() = 173198799278039084
     * comment.getCommentId() = 173198799236096001
     * 	comment.getCommentId() = 173198799282233376
     * comment.getCommentId() = 173198799236096002
     * 	comment.getCommentId() = 173198799278039065
     * comment.getCommentId() = 173198799240290304
     * 	comment.getCommentId() = 173198799278039078
     * comment.getCommentId() = 173198799240290305
     * 	comment.getCommentId() = 173198799273844743
     */

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> responses1 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId={articleId}&pageSize={pageSize}", 1L, 5L)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        for (CommentResponse comment : responses1) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        Long parentCommentId = responses1.getLast().getParentCommentId();
        Long commentId = responses1.getLast().getCommentId();

        List<CommentResponse> responses2 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId={articleId}&pageSize={pageSize}&lastParentCommentId={lastParentCommentId}&lastCommentId={lastCommentId}", 1L, 5L, parentCommentId, commentId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("secondPage");
        for (CommentResponse comment : responses2) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
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
