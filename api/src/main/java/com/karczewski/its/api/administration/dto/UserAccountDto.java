package com.karczewski.its.api.administration.dto;


import lombok.Builder;

import java.util.UUID;

@Builder
public record UserAccountDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        boolean isActive,
        String role,
        DepartmentDto department
) {
}
