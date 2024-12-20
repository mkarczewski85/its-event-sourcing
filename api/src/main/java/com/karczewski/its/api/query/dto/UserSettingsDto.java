package com.karczewski.its.api.query.dto;

import lombok.Builder;

@Builder
public record UserSettingsDto(
        boolean enableIssueNotifications,
        boolean enableCommentNotifications
) {
}
