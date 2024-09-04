package com.soulware.backend.domain.post.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDto {

    private Long postId;
    private String title;
    private String username;
    private LocalDateTime createdAt;

}
