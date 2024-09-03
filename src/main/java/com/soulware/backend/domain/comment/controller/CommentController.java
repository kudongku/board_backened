package com.soulware.backend.domain.comment.controller;

import com.soulware.backend.domain.comment.dto.CommentRequestDto;
import com.soulware.backend.domain.comment.dto.CommentResponseDto;
import com.soulware.backend.domain.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

}
