package com.soulware.backend.domain.post.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    private static final Long userId = 1L;

    @Test
    void createPost() {

        for (int i = 1; i <= 1000; i++) {
            postService.createPost(userId, "대량의 제목 데이터 " + i, "대량의 내용 데이터 " + i);
        }

    }
}