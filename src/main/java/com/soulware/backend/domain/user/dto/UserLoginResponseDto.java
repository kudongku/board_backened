package com.soulware.backend.domain.user.dto;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;

    public UserLoginResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "bearer";
    }
}
