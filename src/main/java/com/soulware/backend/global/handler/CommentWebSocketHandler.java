package com.soulware.backend.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulware.backend.domain.comment.dto.CommentResponseDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j(topic = "CommentWebSocketHandler")
@RequiredArgsConstructor
@Component
public class CommentWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<Long, ConcurrentLinkedQueue<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(
            @NonNull WebSocketSession session
    ) {
        Long postId = Long.parseLong(session.getAttributes().get("postId").toString());
        sessionMap.computeIfAbsent(postId, id -> new ConcurrentLinkedQueue<>()).add(session);

        log.info("comment websocket connect = postId : {}", postId);
    }

    @Override
    protected void handleTextMessage(
            @NonNull WebSocketSession session,
            @NonNull TextMessage message
    ) throws Exception {
        Long postId = Long.parseLong(session.getAttributes().get("postId").toString());
        String content = message.getPayload();
        String username = session.getAttributes().get("username").toString();
        CommentResponseDto commentResponseDto = new CommentResponseDto(content, username);
        ConcurrentLinkedQueue<WebSocketSession> sessions = sessionMap.get(postId);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(commentResponseDto)));
            }
        }

        log.info("comment websocket send = postId : {}, content : {}", postId, content);
    }


    @Override
    public void afterConnectionClosed(
            @NonNull WebSocketSession session,
            @NonNull CloseStatus status
    ) {
        Long postId = Long.parseLong(session.getAttributes().get("postId").toString());
        ConcurrentLinkedQueue<WebSocketSession> sessions = sessionMap.get(postId);
        sessions.remove(session);

        if (sessions.isEmpty()) {
            sessionMap.remove(postId);
        }

        log.info("comment websocket closes = postId : {}", postId);
    }

}
