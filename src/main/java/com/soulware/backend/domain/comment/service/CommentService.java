package com.soulware.backend.domain.comment.service;

import com.soulware.backend.domain.comment.dto.CommentResponseDto;
import com.soulware.backend.domain.comment.entity.Comment;
import com.soulware.backend.domain.comment.repository.CommentRepository;
import com.soulware.backend.domain.post.entity.Post;
import com.soulware.backend.domain.post.service.PostService;
import com.soulware.backend.domain.user.entity.User;
import com.soulware.backend.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long postId) {
        Post post = postService.getPostByPostId(postId);

        return commentRepository.findAllByPost(post)
            .stream()
            .map(comment -> new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername())
            )
            .toList();
    }

    @Transactional
    public void createComment(Long userId, Long postId, String content) {
        User user = userService.getUserByUserId(userId);
        Post post = postService.getPostByPostId(postId);
        Comment comment = new Comment(content, user, post);

        commentRepository.save(comment);
    }
}
