package com.soulware.backend.domain.post.service;

import com.soulware.backend.domain.post.dto.PostResponseDto;
import com.soulware.backend.domain.post.entity.Post;
import com.soulware.backend.domain.post.repository.PostRepository;
import com.soulware.backend.domain.user.entity.User;
import com.soulware.backend.domain.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public void createPost(Long userId, String title, String content) {
        User user = userService.getUserByUserId(userId);
        Post post = new Post(title, content, user);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAll()
            .stream()
            .map(post -> new PostResponseDto(post.getId(), post.getTitle(), post.getContent()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        Post post = getPostByPostId(postId);

        return new PostResponseDto(postId, post.getTitle(), post.getContent());
    }

    @Transactional
    public void updatePost(Long userId, Long postId, String title, String content) {
        Post post = getPostByPostId(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        post.update(title, content);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = getPostByPostId(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
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
