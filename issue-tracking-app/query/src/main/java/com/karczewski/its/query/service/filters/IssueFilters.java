package com.karczewski.its.query.service.filters;

import lombok.*;

@Builder
@RequiredArgsConstructor
public record IssueFilters(String uuid,
                           String titlePhrase,
                           String status,
                           String severity,
                           String type) {

}
