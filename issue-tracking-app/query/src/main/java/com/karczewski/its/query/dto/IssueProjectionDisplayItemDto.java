package com.karczewski.its.query.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IssueProjectionDisplayItemDto(
        String uuid,
        String title,
        String status,
        String severity,
        String type,
        LocalDateTime reportedAt
) {
}
