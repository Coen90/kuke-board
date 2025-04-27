package coen.board.comment.controller;

import coen.board.comment.service.CommentService;
import coen.board.comment.service.request.CommentCreateRequest;
import coen.board.comment.service.response.CommentPageResponse;
import coen.board.comment.service.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/v1/comments/{commentId}")
    public CommentResponse getComment(@PathVariable Long commentId) {
        return commentService.read(commentId);
    }

    @PostMapping("/v1/comments")
    public CommentResponse createComment(@RequestBody CommentCreateRequest commentCreateRequest) {
        return commentService.create(commentCreateRequest);
    }

    @DeleteMapping("/v1/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }

    @GetMapping("/v1/comments")
    public CommentPageResponse getComment(
            @RequestParam Long articleId,
            @RequestParam Long page,
            @RequestParam Long pageSize
    ) {
        return commentService.readAll(articleId, page, pageSize);
    }

    @GetMapping("/v1/comments/infinite-scroll")
    public List<CommentResponse> getComment(
            @RequestParam Long articleId,
            @RequestParam(required = false) Long lastParentCommentId,
            @RequestParam(required = false) Long lastCommentId,
            @RequestParam Long pageSize
    ) {
        return commentService.readAll(articleId, lastParentCommentId, lastCommentId, pageSize);
    }
}
