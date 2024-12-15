package com.karczewski.its.api.query.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Builder
public record IssueProjectionDto(
    UUID uuid,
    String title,
    String description,
    String status,
    String severity,
    String type,
    LocalDateTime reportedAt,
    LocalDateTime updatedAt,
    UserDto reportedBy,
    UserDto assignedTo,
    Collection<AttachmentDto> attachments
) {
}
