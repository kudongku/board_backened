package com.soulware.backend.domain.post.service;

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

    @Transactional
    public Long createPost(Long userId, String title, String content) {
        User user = userService.getUserByUserId(userId);
        Post post = new Post(title, content, user);
        postRepository.save(post);
        return post.getId();
    }

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

        return new PostDetailResponseDto(
            post.getTitle(),
            post.getContent(),
            post.getUser().getUsername(),
            post.getCreatedAt()
        );
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
