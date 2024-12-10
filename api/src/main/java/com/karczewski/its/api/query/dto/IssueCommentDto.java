package com.karczewski.its.api.query.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IssueCommentDto(
        String content,
        UserDto authoredBy,
        LocalDateTime publishedAt
) {
}
