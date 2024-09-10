package com.soulware.backend.domain.comment.repository;

import com.soulware.backend.domain.comment.entity.Comment;
import com.soulware.backend.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Slice<Comment> findAllByPost(Post post, Pageable pageable);
}
