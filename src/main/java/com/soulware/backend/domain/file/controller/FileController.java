package com.soulware.backend.domain.file.controller;

import com.soulware.backend.domain.file.service.FileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
            return new ResponseEntity<>("No file selected", HttpStatus.BAD_REQUEST);
        }

        fileService.uploadImage(postId, postImage);

        return new ResponseEntity<>("File uploaded successfully: ", HttpStatus.OK);
    }

}
