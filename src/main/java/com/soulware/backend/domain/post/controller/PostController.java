package com.soulware.backend.domain.post.controller;

import com.soulware.backend.domain.post.dto.PostRequestDto;
import com.soulware.backend.domain.post.dto.PostResponseDto;
import com.soulware.backend.domain.post.service.PostService;
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
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        List<PostResponseDto> posts = postService.getPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
        @PathVariable Long postId
    ) {
        PostResponseDto postResponseDto = postService.getPost(postId);

        return ResponseEntity.ok(postResponseDto);
    }

    @PostMapping
    public ResponseEntity<String> createPost(
        Authentication authentication,
        @RequestBody PostRequestDto postRequestDto
    ) {
        postService.createPost(
            (Long) authentication.getPrincipal(),
            postRequestDto.getTitle(),
            postRequestDto.getContent()
        );

        return ResponseEntity.ok("게시물 생성이 완료되었습니다.");
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(
        Authentication authentication,
        @RequestBody PostRequestDto postRequestDto,
        @PathVariable Long postId
    ) {
        postService.updatePost(
            (Long) authentication.getPrincipal(),
            postId,
            postRequestDto.getTitle(),
            postRequestDto.getContent()
        );

        return ResponseEntity.ok("게시물 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
        Authentication authentication,
        @PathVariable Long postId
    ) {
        postService.deletePost(
            (Long) authentication.getPrincipal(),
            postId
        );

        return ResponseEntity.ok("게시물 삭제가 완료되었습니다.");
    }

}
