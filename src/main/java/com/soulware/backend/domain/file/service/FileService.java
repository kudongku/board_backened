package com.soulware.backend.domain.file.service;

import com.soulware.backend.domain.file.entity.File;
import com.soulware.backend.domain.file.repository.FileRepository;
import com.soulware.backend.domain.post.entity.Post;
import com.soulware.backend.domain.post.service.PostService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FileService {
    private static final String FILE_DIRECTORY = "/Users/kudonghyun/Desktop/soulware/board/backend/src/main/resources/images/";
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
    }
}
