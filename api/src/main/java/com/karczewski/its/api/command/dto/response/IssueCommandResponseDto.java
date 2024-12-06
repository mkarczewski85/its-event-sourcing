package com.karczewski.its.api.command.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record IssueCommandResponseDto(UUID uuid) {}
