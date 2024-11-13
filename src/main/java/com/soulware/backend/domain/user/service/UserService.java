package com.soulware.backend.domain.user.service;

import com.soulware.backend.domain.user.dto.UserLoginResponseDto;
import com.soulware.backend.domain.user.entity.User;
import com.soulware.backend.domain.user.repository.UserRepository;
import com.soulware.backend.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final static Long ACCESS_TOKEN_EXPIRED_MS = 1000 * 60L; // 1 minute
    private final static Long REFRESH_TOKEN_EXPIRED_MS = 1000 * 60 * 60 * 24 * 7L; // 1 day

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(
            String username,
            String password,
            String passwordConfirm
    ) {
        validateUserName(username);
        String encodedPassword = encodePassword(password, passwordConfirm);
        User user = new User(username, encodedPassword);

        userRepository.save(user);
    }

    @Transactional
    public UserLoginResponseDto login(
            String username,
            String password
    ) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("잘못된 아이디를 입력했습니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호를 입력했습니다.");
        }

        String accessToken = jwtUtil.createJwt(user.getId(), username, ACCESS_TOKEN_EXPIRED_MS);
        String refreshToken = jwtUtil.createJwt(user.getId(), username, REFRESH_TOKEN_EXPIRED_MS);
        return new UserLoginResponseDto(
                accessToken,
                refreshToken,
                username
        );
    }

    @Transactional(readOnly = true)
    public UserLoginResponseDto refresh(String refreshToken) {
        jwtUtil.isExpired(refreshToken);
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = getUserByUserId(userId);
        String accessToken = jwtUtil.createJwt(userId, user.getUsername(), ACCESS_TOKEN_EXPIRED_MS);
        return new UserLoginResponseDto(
                accessToken,
                refreshToken,
                user.getUsername()
        );
    }

    @Transactional(readOnly = true)
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않는 아이디입니다.")
        );
    }

    private void validateUserName(String username) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("동일한 아이디가 존재합니다.");
        }

    }

    private String encodePassword(String password, String passwordConfirm) {

        if (!password.equals(passwordConfirm)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return passwordEncoder.encode(password);
    }

}
