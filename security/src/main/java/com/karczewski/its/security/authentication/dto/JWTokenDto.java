package com.karczewski.its.security.authentication.dto;

import lombok.Builder;

@Builder
public record JWTokenDto(
        String token,
        Long expiresIn,
        LoggedUserDto userInfo
) {
}
