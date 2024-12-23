package com.soulware.backend.domain.user.controller;

import com.soulware.backend.domain.user.dto.UserLoginRequestDto;
import com.soulware.backend.domain.user.dto.UserLoginResponseDto;
import com.soulware.backend.domain.user.dto.UserRefreshRequestDto;
import com.soulware.backend.domain.user.dto.UserSignupRequestDto;
import com.soulware.backend.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> signup(
        @RequestBody UserSignupRequestDto usersignupRequestDto
    ) {
        userService.signup(
            usersignupRequestDto.getUsername(),
            usersignupRequestDto.getPassword(),
            usersignupRequestDto.getPasswordConfirm()
        );

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(
        @RequestBody UserLoginRequestDto userLoginRequestDto,
        HttpServletResponse response
    ) {
        UserLoginResponseDto userLoginResponseDto = userService.login(
            userLoginRequestDto.getUsername(),
            userLoginRequestDto.getPassword()
        );

        return ResponseEntity.ok(userLoginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserLoginResponseDto> refresh(
            @RequestBody UserRefreshRequestDto userRefreshRequestDto
    ){
        UserLoginResponseDto userLoginResponseDto = userService.refresh(
            userRefreshRequestDto.getRefreshToken()
        );
        return ResponseEntity.ok(userLoginResponseDto);
    }
}
