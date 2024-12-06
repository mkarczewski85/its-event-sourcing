package com.karczewski.its.query.service.filters;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Builder
@RequiredArgsConstructor
public record IssueFilters(UUID projectionUuid,
                           UUID userUuid,
                           String titlePhrase,
                           String status,
                           String severity,
                           String type) {

}
