package com.soulware.backend.domain.post.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponseDto {

    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private boolean hasFile;

}
