package com.soulware.backend.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulware.backend.domain.comment.dto.CommentRequestDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;
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
        Long postId = getPostIdFromSession(session);
        sessionMap.computeIfAbsent(postId, id -> new ConcurrentLinkedQueue<>()).add(session);

        log.info("comment websocket connect = postId : {}", postId);
    }

    @Override
    protected void handleTextMessage(
            @NonNull WebSocketSession session,
            @NonNull TextMessage message
    ) throws Exception {
        Long postId = getPostIdFromSession(session);
        String content = message.getPayload();
        CommentRequestDto commentRequestDto = new CommentRequestDto(content);
        ConcurrentLinkedQueue<WebSocketSession> sessions = sessionMap.get(postId);

        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(objectMapper.writeValueAsString(commentRequestDto)));
                }
            }
        }

        log.info("comment websocket send = postId : {}, content : {}", postId, content);
    }


    @Override
    public void afterConnectionClosed(
            @NonNull WebSocketSession session,
            @NonNull CloseStatus status
    ) {
        Long postId = getPostIdFromSession(session);
        ConcurrentLinkedQueue<WebSocketSession> sessions = sessionMap.get(postId);

        if (sessions != null) {
            sessions.remove(session);

            if (sessions.isEmpty()) {
                sessionMap.remove(postId);
            }
        }

        log.info("comment websocket closes = postId : {}", postId);
    }

    private Long getPostIdFromSession(WebSocketSession session) {
        try {
            String query = Objects.requireNonNull(session.getUri()).getQuery();
            String[] params = query.split("=");
            if (params.length == 2 && "postId".equals(params[0])) {
                return Long.valueOf(params[1]);
            }
            throw new IllegalArgumentException("Invalid query parameter: " + query);
        } catch (Exception e) {
            log.error("Failed to extract postId from session URI", e);
            throw new RuntimeException("Invalid WebSocket session: postId is required");
        }
    }

}
