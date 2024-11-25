package com.soulware.backend.domain.comment.controller;

import com.soulware.backend.domain.comment.dto.CommentRequestDto;
import com.soulware.backend.domain.comment.dto.CommentResponseDto;
import com.soulware.backend.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class CommentStompController {

    private final CommentService commentService;

    @MessageMapping("/posts/{postId}/comments")
    @SendTo("/sub/posts/{postId}/comments")
    public CommentResponseDto handleComment(
            @DestinationVariable Long postId,
            CommentRequestDto commentRequestDto,
            StompHeaderAccessor accessor
    ) {
        Long userId = (Long) Objects.requireNonNull(accessor.getSessionAttributes()).get("userId");
        return commentService.createComment(userId, postId, commentRequestDto.getContent());
    }

}
