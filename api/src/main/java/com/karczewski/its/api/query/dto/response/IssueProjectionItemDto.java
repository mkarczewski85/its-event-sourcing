package com.karczewski.its.api.query.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IssueProjectionItemDto(
        UUID uuid,
        String title,
        String status,
        String severity,
        String type,
        LocalDateTime reportedAt
) {
}
