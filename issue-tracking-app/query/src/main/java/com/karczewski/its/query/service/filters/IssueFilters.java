package com.karczewski.its.query.service.filters;

import lombok.Builder;

import java.util.UUID;

@Builder
public record IssueFilters(UUID projectionUuid,
                           UUID userUuid,
                           String titlePhrase,
                           String status,
                           String severity,
                           String type) {

}
