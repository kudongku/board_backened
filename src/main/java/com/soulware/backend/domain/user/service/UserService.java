package com.soulware.backend.domain.user.service;

import com.soulware.backend.domain.user.entity.User;
import com.soulware.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    //    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

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

//    public String login(
//        String username,
//        String password,
//        HttpServletResponse response
//    ) {
//        String bearerToken = jwtUtil.createJwt(username);
//        response.addHeader(HttpHeaders.AUTHORIZATION, bearerToken);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        return bearerToken;
//    }

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
