package com.karczewski.its.api.query.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IssueCommentDto(
        String content,
        UserDto authoredBy,
        LocalDateTime publishedAt
) {
}
