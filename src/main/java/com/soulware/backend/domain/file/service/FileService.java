package com.soulware.backend.domain.file.service;

import com.soulware.backend.domain.file.dto.FileCreateResponseDto;
import com.soulware.backend.domain.file.dto.FileDownloadResponseDto;
import com.soulware.backend.domain.file.entity.File;
import com.soulware.backend.domain.file.repository.FileRepository;
import com.soulware.backend.domain.user.entity.User;
import com.soulware.backend.domain.user.service.UserService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
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
    private final UserService userService;

    @Transactional
    public FileCreateResponseDto uploadFile(
        Long userId,
        MultipartFile postFile
    ) throws IOException {
        User user = userService.getUserByUserId(userId);

        String originalFileName = Objects.requireNonNull(
            postFile.getOriginalFilename()
        ).replaceAll(" ", "");
        String fileName = UUID.randomUUID() + "_" + originalFileName;
        Path filePath = Paths.get(FILE_DIRECTORY + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, postFile.getBytes());

        File file = new File(originalFileName, fileName, user);
        fileRepository.save(file);

        return new FileCreateResponseDto(file.getId());
    }

    @Transactional(readOnly = true)
    public FileDownloadResponseDto downloadFile(
        Long postId
    ) throws MalformedURLException {
        File file = fileRepository.findByPostId(postId).orElseThrow(
            () -> new NullPointerException("이미지가 존재하지 않습니다.")
        );
        Path filePath = Paths.get(FILE_DIRECTORY + file.getFileName());
        Resource resource = new UrlResource(filePath.toUri());

        return new FileDownloadResponseDto(file.getOriginalFileName(), resource);
    }

    @Transactional
    public FileCreateResponseDto updateFile(
        Long userId,
        Long postId,
        MultipartFile postFile
    ) throws IOException {
        User user = userService.getUserByUserId(userId);
        File file = fileRepository.findByPostId(postId).orElse(null);

        String originalFileName = postFile.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + postFile.getOriginalFilename();
        Path filePath = Paths.get(FILE_DIRECTORY + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, postFile.getBytes());

        if (file == null) {
            file = new File(originalFileName, fileName, user);
            fileRepository.save(file);
            return new FileCreateResponseDto(file.getId());
        }

        file.updateFileName(originalFileName, fileName);
        return new FileCreateResponseDto(file.getId());
    }

    @Transactional
    public void deleteFile(
        Long userId,
        Long postId
    ) {
        File file = fileRepository.findByPostId(postId).orElseThrow(
            () -> new NullPointerException("해당하는 이미지가 존재하지 않습니다.")
        );

        if (!file.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        fileRepository.delete(file);
    }

    @Transactional(readOnly = true)
    public File getFileByFileId(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(
            () -> new NullPointerException("해당하는 사진이 존재하지 않습니다.")
        );
    }

}
