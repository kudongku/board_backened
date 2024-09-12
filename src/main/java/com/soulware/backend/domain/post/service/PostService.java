package com.soulware.backend.domain.post.service;

import com.soulware.backend.domain.file.entity.File;
import com.soulware.backend.domain.file.service.FileService;
import com.soulware.backend.domain.post.dto.PostDetailResponseDto;
import com.soulware.backend.domain.post.dto.PostListResponseDto;
import com.soulware.backend.domain.post.entity.Post;
import com.soulware.backend.domain.post.repository.PostRepository;
import com.soulware.backend.domain.user.entity.User;
import com.soulware.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public Slice<PostListResponseDto> getPosts(Pageable pageable) {
        Slice<Post> posts = postRepository.findAll(pageable);

        return posts.map(post -> new PostListResponseDto(
            post.getId(),
            post.getTitle(),
            post.getUser().getUsername(),
            post.getCreatedAt()
        ));
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto getPost(Long postId) {
        Post post = getPostByPostId(postId);
        boolean hasFile = post.getFile() != null;

        return new PostDetailResponseDto(
            post.getTitle(),
            post.getContent(),
            post.getUser().getUsername(),
            post.getCreatedAt(),
            hasFile
        );
    }

    @Transactional
    public void createPost(
        Long userId,
        String title,
        String content,
        Long fileId
    ) {
        User user = userService.getUserByUserId(userId);
        File file = fileService.getFileByFileId(fileId);

        if (!file.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Post post = new Post(title, content, user, file);

        postRepository.save(post);
        file.setPost(post);
    }

    @Transactional
    public void createPost(
        Long userId,
        String title,
        String content
    ) {
        User user = userService.getUserByUserId(userId);
        Post post = new Post(title, content, user);

        postRepository.save(post);
    }

    @Transactional
    public void updatePost(
        Long userId,
        Long postId,
        String title,
        String content,
        Long fileId
    ) {
        Post post = getPostByPostId(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        if (post.getFile() == null) {
            File file = fileService.getFileByFileId(fileId);
            file.setPost(post);
            post.setFile(file);
        }

        post.update(title, content);
    }

    @Transactional
    public void updatePost(
        Long userId,
        Long postId,
        String title,
        String content
    ) {
        Post post = getPostByPostId(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        if(post.getFile() != null){
            post.setFile(null);
        }

        post.update(title, content);
    }

    @Transactional
    public void deleteFile(
        Long userId,
        Long postId
    ) {
        Post post = getPostByPostId(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        if(post.getFile()==null){
            throw new NullPointerException("파일이 없는 게시물입니다.");
        }

        post.setFile(null);
        fileService.deleteFile(userId, postId);
    }


    @Transactional
    public void deletePost(
        Long userId,
        Long postId)
    {
        Post post = getPostByPostId(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        if(post.getFile()!=null){
            fileService.deleteFile(userId, postId);
        }

        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Post getPostByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> new NullPointerException("해당하는 게시물이 존재하지 않습니다.")
        );
    }
}
