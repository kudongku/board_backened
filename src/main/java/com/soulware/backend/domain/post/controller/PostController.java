package com.soulware.backend.domain.post.controller;

import com.soulware.backend.domain.post.dto.PostDetailResponseDto;
import com.soulware.backend.domain.post.dto.PostListResponseDto;
import com.soulware.backend.domain.post.dto.PostRequestDto;
import com.soulware.backend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public ResponseEntity<Slice<PostListResponseDto>> getPosts(
        Pageable pageable
    ) {
        Slice<PostListResponseDto> pagedPosts = postService.getPosts(pageable);

        return ResponseEntity.ok(pagedPosts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(
        @PathVariable Long postId
    ) {
        PostDetailResponseDto postDetailResponseDto = postService.getPost(postId);

        return ResponseEntity.ok(postDetailResponseDto);
    }

    @PostMapping
    public ResponseEntity<String> createPost(
        Authentication authentication,
        @RequestBody PostRequestDto postRequestDto
    ) {
        if (postRequestDto.getFileId() != null) {
            postService.createPost(
                (Long) authentication.getPrincipal(),
                postRequestDto.getTitle(),
                postRequestDto.getContent(),
                postRequestDto.getFileId()
            );
        } else {
            postService.createPost(
                (Long) authentication.getPrincipal(),
                postRequestDto.getTitle(),
                postRequestDto.getContent()
            );
        }

        return ResponseEntity.ok("게시물 생성이 완료되었습니다.");
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(
        Authentication authentication,
        @RequestBody PostRequestDto postRequestDto,
        @PathVariable Long postId
    ) {
        if (postRequestDto.getFileId() != null) {
            postService.updatePost(
                (Long) authentication.getPrincipal(),
                postId,
                postRequestDto.getTitle(),
                postRequestDto.getContent(),
                postRequestDto.getFileId()
            );
        }else{
            postService.updatePost(
                (Long) authentication.getPrincipal(),
                postId,
                postRequestDto.getTitle(),
                postRequestDto.getContent()
            );
        }

        return ResponseEntity.ok("게시물 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{postId}/files")
    public ResponseEntity<String> deleteFile(
        Authentication authentication,
        @PathVariable Long postId
    ) {
        postService.deleteFile(
            (Long) authentication.getPrincipal(),
            postId
        );

        return ResponseEntity.ok("file 삭제가 완료되었습니다.");
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
