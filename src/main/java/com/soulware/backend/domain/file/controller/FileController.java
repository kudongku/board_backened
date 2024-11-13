package com.soulware.backend.domain.file.controller;

import com.soulware.backend.domain.file.dto.FileCreateResponseDto;
import com.soulware.backend.domain.file.dto.FileDownloadResponseDto;
import com.soulware.backend.domain.file.service.FileService;
import java.io.IOException;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/files")
    public ResponseEntity<FileCreateResponseDto> uploadFile(
        Authentication authentication,
        @RequestPart("postFile") MultipartFile postFile
    ) throws IOException {

        if (postFile.isEmpty()) {
            throw new NullPointerException("file이 존재하지 않습니다.");
        }

        FileCreateResponseDto fileCreateResponseDto = fileService.uploadFile(
            (Long) authentication.getPrincipal(),
            postFile
        );

        return ResponseEntity.ok(fileCreateResponseDto);
    }

    @GetMapping("/{postId}/files")
    public ResponseEntity<Resource> downloadFile(
        @PathVariable Long postId
    ) throws MalformedURLException {
        FileDownloadResponseDto fileDownloadResponseDto = fileService.downloadFile(postId);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "filename=" + fileDownloadResponseDto.getFileName()
            )
            .body(fileDownloadResponseDto.getResource());
    }

    @PutMapping("/{postId}/files")
    public ResponseEntity<FileCreateResponseDto> updateFile(
        Authentication authentication,
        @RequestPart("postFile") MultipartFile postImage,
        @PathVariable Long postId
    ) throws IOException {

        if (postImage.isEmpty()) {
            throw new NullPointerException("file이 존재하지 않습니다.");
        }

        FileCreateResponseDto fileCreateResponseDto = fileService.updateFile(
            (Long) authentication.getPrincipal(),
            postId,
            postImage
        );

        return ResponseEntity.ok(fileCreateResponseDto);
    }

}
