package com.karczewski.its.api.query.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IssueDto(
    UUID uuid,
    String title,
    String description,
    String status,
    String severity,
    String type,
    LocalDateTime reportedAt,
    UUID reportedBy,
    UUID assignedTo
) {
}
