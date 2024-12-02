package com.karczewski.its.query.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IssueCommentDto(
        UUID issueUuid,
        String content,
        UUID authoredBy,
        LocalDateTime publishedAt
) {
}
