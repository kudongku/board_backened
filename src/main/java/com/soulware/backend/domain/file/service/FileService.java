package com.soulware.backend.domain.file.service;

import com.soulware.backend.domain.file.entity.File;
import com.soulware.backend.domain.file.repository.FileRepository;
import com.soulware.backend.domain.post.entity.Post;
import com.soulware.backend.domain.post.service.PostService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.directory}")
    private String FILE_DIRECTORY;

    private final FileRepository fileRepository;
    private final PostService postService;

    @Transactional
    public void uploadImage(
        Long postId,
        MultipartFile postImage
    ) throws IOException {
        Post post = postService.getPostByPostId(postId);

        String fileName = UUID.randomUUID() + "_" + postImage.getOriginalFilename();
        Path filePath = Paths.get(FILE_DIRECTORY + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, postImage.getBytes());

        File file = new File(fileName, post);
        fileRepository.save(file);
        post.setFile(file);
    }

    @Transactional(readOnly = true)
    public Resource getFiles(
        Long postId
    ) throws MalformedURLException {
        Post post = postService.getPostByPostId(postId);
        File file = fileRepository.findByPost(post).orElseThrow(
            () -> new NullPointerException("해당하는 이미지가 존재하지 않습니다.")
        );
        Path filePath = Paths.get(FILE_DIRECTORY + file.getFileName());

        return new UrlResource(filePath.toUri());
    }

    @Transactional
    public void updateImage(
        Long userId,
        Long postId,
        MultipartFile postImage
    ) throws IOException {
        Post post = postService.getPostByPostId(postId);

        if(!post.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        File file = fileRepository.findByPost(post).orElse(null);

        String fileName = UUID.randomUUID() + "_" + postImage.getOriginalFilename();
        Path filePath = Paths.get(FILE_DIRECTORY + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, postImage.getBytes());

        if (file == null) {
            file = new File(fileName, post);
            fileRepository.save(file);
            post.setFile(file);
            return;
        }

        file.updateFileName(fileName);
    }

    @Transactional
    public void deleteImage(Long userId, Long postId) {
        Post post = postService.getPostByPostId(postId);
        File file = fileRepository.findByPost(post).orElseThrow(
            () -> new NullPointerException("해당하는 이미지가 존재하지 않습니다.")
        );

        if(!post.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        post.removeFile();
        fileRepository.delete(file);
    }
}
