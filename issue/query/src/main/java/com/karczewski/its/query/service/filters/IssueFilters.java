package com.karczewski.its.query.service.filters;

import com.karczewski.its.query.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
public class IssueFilters {
    UUID projectionId;
    @Setter
    User user;
    String titlePhrase;
    String status;
    String severity;
    String type;
}
