package com.soulware.backend.domain.file.controller;

import com.soulware.backend.domain.file.service.FileService;
import java.io.IOException;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class FileController {

    private final FileService fileService;

    @PostMapping("/{postId}/files")
    public ResponseEntity<String> uploadImage(
        @RequestPart("postImage") MultipartFile postImage,
        @PathVariable Long postId
    ) throws IOException {

        if (postImage.isEmpty()) {
            throw new NullPointerException("file이 존재하지 않습니다.");
        }

        fileService.uploadImage(postId, postImage);

        return ResponseEntity.ok("file 업로드가 완료되었습니다.");
    }

    @GetMapping("/{postId}/files")
    public ResponseEntity<Resource> getFiles(
        @PathVariable Long postId
    ) throws MalformedURLException {
        Resource resource = fileService.getFiles(postId);

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource);
    }

    @PutMapping("/{postId}/files")
    public ResponseEntity<String> updateImage(
        Authentication authentication,
        @RequestPart("postImage") MultipartFile postImage,
        @PathVariable Long postId
    ) throws IOException {

        if (postImage.isEmpty()) {
            throw new NullPointerException("file이 존재하지 않습니다.");
        }

        fileService.updateImage((Long) authentication.getPrincipal(), postId, postImage);

        return ResponseEntity.ok("file 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{postId}/files")
    public ResponseEntity<String> deleteImage(
        Authentication authentication,
        @PathVariable Long postId
    ) {
        fileService.deleteImage((Long) authentication.getPrincipal(), postId);

        return ResponseEntity.ok("file 삭제가 완료되었습니다.");
    }
}
