package com.soulware.backend.domain.user.dto;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final String username;

    public UserLoginResponseDto(String accessToken, String refreshToken, String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "bearer";
        this.username = username;
    }
}
