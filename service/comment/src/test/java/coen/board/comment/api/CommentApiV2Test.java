package coen.board.comment.api;

import coen.board.comment.service.response.CommentPageResponse;
import coen.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiV2Test {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = create(new CommentCreateRequestV2(1L, "My comment1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2(1L, "My comment2", response1.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2(1L, "My comment3", response2.getPath(), 1L));

        System.out.println("response1.getPath() = " + response1.getPath());
        System.out.println("response1.getCommentId() = " + response1.getCommentId());
        System.out.println("\tresponse2.getPath() = " + response2.getPath());
        System.out.println("\tresponse2.getCommentId() = " + response2.getCommentId());
        System.out.println("\t\tresponse3.getPath() = " + response3.getPath());
        System.out.println("\t\tresponse3.getCommentId() = " + response3.getCommentId());

        /**
         * response1.getPath() = 00002
         * response1.getCommentId() = 174657826420183040
         * 	  response2.getPath() = 0000200000
         * 	  response2.getCommentId() = 174657982435708928
         * 		response3.getPath() = 000020000000000
         * 		response3.getCommentId() = 174657987686977536
         */
    }

    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                .uri("/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("v2/comments/{commentId}", 174657826420183040L)
                .retrieve()
                .body(CommentResponse.class);
        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("v2/comments/{commentId}", 174657826420183040L)
                .retrieve()
                .body(Void.class);
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("v2/comments?articleId={articleId}&page={page}&pageSize={pageSize}", 1L, 1L, 10L)
                .retrieve()
                .body(CommentPageResponse.class);
        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        /**
         * comment.getCommentId() = 174654558439100416
         * comment.getCommentId() = 174654559013720064
         * comment.getCommentId() = 174654559068246016
         * comment.getCommentId() = 174654723329773568
         * comment.getCommentId() = 174654723501740032
         *
         * comment.getCommentId() = 174654723560460288
         * comment.getCommentId() = 174657826420183040
         * comment.getCommentId() = 174657982435708928
         * comment.getCommentId() = 174657987686977536
         * comment.getCommentId() = 174660203277332486
         */
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> response1 = restClient.get()
                .uri("v2/comments/infinite-scroll?articleId=1&pageSize=5", 1L, 5L)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        for (CommentResponse response : response1) {
            System.out.println("response.getCommentId() = " + response.getCommentId());
        }

        String lastPath = response1.getLast().getPath();
        List<CommentResponse> response2 = restClient.get()
                .uri("v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath={lastPath}", lastPath)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });
        System.out.println("secondPage");
        for (CommentResponse response : response2) {
            System.out.println("response.getCommentId() = " + response.getCommentId());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }
}
