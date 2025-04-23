package coen.board.comment.controller;

import coen.board.comment.service.CommentService;
import coen.board.comment.service.request.CommentCreateRequest;
import coen.board.comment.service.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
