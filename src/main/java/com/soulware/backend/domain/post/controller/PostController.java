package com.soulware.backend.domain.post.controller;

import com.soulware.backend.domain.post.dto.PostCreateRequestDto;
import com.soulware.backend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> createPost(
        Authentication authentication,
        @RequestBody PostCreateRequestDto postCreateRequestDto
    ) {
        postService.createPost(
            (Long) authentication.getPrincipal(),
            postCreateRequestDto.getTitle(),
            postCreateRequestDto.getContent()
        );

        return ResponseEntity.ok("게시물 생성이 완료되었습니다.");
    }

}
