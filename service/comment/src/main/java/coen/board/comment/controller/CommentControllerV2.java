package coen.board.comment.controller;

import coen.board.comment.service.CommentServiceV2;
import coen.board.comment.service.request.CommentCreateRequestV2;
import coen.board.comment.service.response.CommentPageResponse;
import coen.board.comment.service.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentControllerV2 {
    private final CommentServiceV2 commentService;

    @GetMapping("/v2/comments/{commentId}")
    public CommentResponse getComment(@PathVariable Long commentId) {
        return commentService.read(commentId);
    }

    @PostMapping("/v2/comments")
    public CommentResponse createComment(@RequestBody CommentCreateRequestV2 request) {
        return commentService.create(request);
    }

    @DeleteMapping("/v2/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }

    @GetMapping("/v2/comments")
    public CommentPageResponse readAll(
            @RequestParam(name = "articleId") Long articleId,
            @RequestParam(name = "page") Long page,
            @RequestParam(name = "pageSize") Long pageSize
    ) {
        return commentService.readAll(articleId, page, pageSize);
    }

    @GetMapping("/v2/comments/infinite-scroll")
    public List<CommentResponse> readAll(
            @RequestParam(name = "articleId") Long articleId,
            @RequestParam(name = "lastPath", required = false) String lastPath,
            @RequestParam(name = "pageSize") Long pageSize
    ) {
        return commentService.readAllInfiniteScroll(articleId, lastPath, pageSize);
    }
}
