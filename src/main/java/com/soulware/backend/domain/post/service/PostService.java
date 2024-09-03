package com.soulware.backend.domain.post.service;

import com.soulware.backend.domain.post.dto.PostListResponseDto;
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
    public List<PostListResponseDto> getPosts() {
        return postRepository.findAll()
            .stream()
            .map(post -> new PostListResponseDto(post.getId(), post.getTitle(), post.getContent()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostListResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new NullPointerException("해당하는 게시물이 존재하지 않습니다.")
        );

        return new PostListResponseDto(postId, post.getTitle(), post.getContent());
    }
}
