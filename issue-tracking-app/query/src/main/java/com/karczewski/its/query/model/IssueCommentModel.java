package com.karczewski.its.query.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IssueCommentModel(
        UUID issueUuid,
        String content,
        UUID authoredBy,
        LocalDateTime publishedAt
) {
}
