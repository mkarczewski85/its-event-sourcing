package com.karczewski.its.api.query.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AttachmentDto(
        UUID uuid,
        String name,
        String contentType) {
}
