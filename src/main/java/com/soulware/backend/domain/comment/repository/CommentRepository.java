package com.soulware.backend.domain.comment.repository;

import com.soulware.backend.domain.comment.entity.Comment;
import com.soulware.backend.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
}
