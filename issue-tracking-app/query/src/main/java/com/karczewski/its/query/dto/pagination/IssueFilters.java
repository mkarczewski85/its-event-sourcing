package com.karczewski.its.query.dto.pagination;

import lombok.*;

@Builder
@RequiredArgsConstructor
public record IssueFilters(String uuid,
                           String titlePhrase,
                           String status,
                           String severity,
                           String type) {

}
