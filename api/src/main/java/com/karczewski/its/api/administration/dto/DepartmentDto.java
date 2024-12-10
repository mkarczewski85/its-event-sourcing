package com.karczewski.its.api.administration.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DepartmentDto(
        UUID uuid,
        String name,
        String location
) {
}
