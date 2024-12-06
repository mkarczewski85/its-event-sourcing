package com.karczewski.its.api.query.dto.response;

import lombok.Builder;

@Builder
public record UserDto(
        String firstName,
        String lastName,
        String email,
        String department
) {}
