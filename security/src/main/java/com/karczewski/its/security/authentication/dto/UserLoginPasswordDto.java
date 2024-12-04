package com.karczewski.its.security.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginPasswordDto(
        @NotBlank String login,
        @NotBlank String password
) { }
