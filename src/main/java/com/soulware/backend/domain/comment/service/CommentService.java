package com.soulware.backend.domain.comment.service;

import com.soulware.backend.domain.comment.dto.CommentResponseDto;
import com.soulware.backend.domain.comment.entity.Comment;
import com.soulware.backend.domain.comment.repository.CommentRepository;
import com.soulware.backend.domain.post.entity.Post;
import com.soulware.backend.domain.post.service.PostService;
import com.soulware.backend.domain.user.entity.User;
import com.soulware.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Slice<CommentResponseDto> getComments(
            Long postId,
            Pageable pageable
    ) {
        Post post = postService.getPostByPostId(postId);
        Slice<Comment> comments = commentRepository.findAllByPost(post, pageable);

        return comments.map(comment -> new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername()
        ));
    }

    @Transactional
    public CommentResponseDto createComment(Long userId, Long postId, String content) {
        User user = userService.getUserByUserId(userId);
        Post post = postService.getPostByPostId(postId);
        Comment comment = new Comment(content, user, post);
        post.addComment(comment);

        commentRepository.save(comment);

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                user.getUsername()
        );
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, String content) {
        User user = userService.getUserByUserId(userId);
        Comment comment = getCommentByCommentId(commentId);

        if (!user.equals(comment.getUser())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        comment.update(content);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId, Long postId) {
        User user = userService.getUserByUserId(userId);
        Post post = postService.getPostByPostId(postId);
        Comment comment = getCommentByCommentId(commentId);

        if (!user.equals(comment.getUser())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        post.removeComment(comment);
        commentRepository.delete(comment);
    }

    public Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 코멘트입니다.")
        );
    }
}
