package com.soulware.backend.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private String content;
    private String writerUsername;

    public CommentResponseDto(String content, String writerUsername) {
        this.content = content;
        this.writerUsername = writerUsername;
    }

}
