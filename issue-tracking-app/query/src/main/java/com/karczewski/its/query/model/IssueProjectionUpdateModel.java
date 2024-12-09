package com.karczewski.its.query.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IssueProjectionUpdateModel(
        UUID uuid,
        String title,
        String description,
        String status,
        String severity,
        String type,
        LocalDateTime reportedAt,
        LocalDateTime updatedAt,
        UUID reportedBy,
        UUID assignedTo
) {
}
