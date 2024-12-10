package com.karczewski.its.api.query.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {}
