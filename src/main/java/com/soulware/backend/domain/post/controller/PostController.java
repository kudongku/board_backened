package com.soulware.backend.domain.post.controller;

import com.soulware.backend.domain.post.dto.PostCreateRequestDto;
import com.soulware.backend.domain.post.dto.PostListResponseDto;
import com.soulware.backend.domain.post.service.PostService;
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
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostListResponseDto>> getPosts() {
        List<PostListResponseDto> posts = postService.getPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostListResponseDto> getPost(
        @PathVariable Long postId
    ) {
        PostListResponseDto postListResponseDto = postService.getPost(postId);

        return ResponseEntity.ok(postListResponseDto);
    }


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
