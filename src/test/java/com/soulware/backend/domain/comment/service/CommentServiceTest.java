package com.soulware.backend.domain.comment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Test
    void createComment() {

        for (int i = 0; i < 1000; i++) {
            commentService.createComment(1L, 1035L, i + 1 + "번째 댓글");
        }

    }
}