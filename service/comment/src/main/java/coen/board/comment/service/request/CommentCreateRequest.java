package coen.board.comment.service.request;

import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private Long articleId;
    private String content;
    private Long parentId;
    private Long writerId;
}
