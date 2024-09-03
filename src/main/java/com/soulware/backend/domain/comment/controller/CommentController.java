package com.soulware.backend.domain.comment.controller;

import com.soulware.backend.domain.comment.dto.CommentRequestDto;
import com.soulware.backend.domain.comment.dto.CommentResponseDto;
import com.soulware.backend.domain.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(
        @PathVariable Long postId
    ) {
        List<CommentResponseDto> commentResponseDto = commentService.getComments(postId);

        return ResponseEntity.ok(commentResponseDto);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<String> createComment(
        Authentication authentication,
        @PathVariable Long postId,
        @RequestBody CommentRequestDto commentRequestDto
    ) {
        commentService.createComment(
            (Long) authentication.getPrincipal(),
            postId,
            commentRequestDto.getContent()
        );

        return ResponseEntity.ok("댓글 생성이 완료되었습니다.");
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> updateComment(
        Authentication authentication,
        @PathVariable Long commentId,
        @RequestBody CommentRequestDto commentRequestDto
    ) {
        commentService.updateComment(
            (Long) authentication.getPrincipal(),
            commentId,
            commentRequestDto.getContent()
        );

        return ResponseEntity.ok("댓글 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
        Authentication authentication,
        @PathVariable Long commentId
    ) {
        commentService.deleteComment(
            (Long) authentication.getPrincipal(),
            commentId
        );

        return ResponseEntity.ok("댓글 삭제가 완료되었습니다.");
    }

}
