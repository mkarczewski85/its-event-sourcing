package com.karczewski.its.api.query.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IssueDisplayItemDto(
        String uuid,
        String title,
        String status,
        String severity,
        String type,
        LocalDateTime reportedAt
) {
}
