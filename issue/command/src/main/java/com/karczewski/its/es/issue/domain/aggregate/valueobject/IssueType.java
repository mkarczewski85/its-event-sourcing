package com.karczewski.its.es.issue.domain.aggregate.valueobject;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class IssueType {

    private final String value;

    public static final IssueType PROBLEM = new IssueType("PROBLEM");
    public static final IssueType INCIDENT = new IssueType("INCIDENT");
    public static final IssueType CHANGE_REQUEST = new IssueType("CHANGE_REQUEST");
    public static final IssueType SERVICE_REQUEST = new IssueType("SERVICE_REQUEST");

    private static final Map<String, IssueType> NAME_TO_ISSUE_TYPE = Map.of(
            PROBLEM.getValue(), PROBLEM,
            INCIDENT.getValue(), INCIDENT,
            CHANGE_REQUEST.getValue(), CHANGE_REQUEST,
            SERVICE_REQUEST.getValue(), SERVICE_REQUEST
    );

    public boolean hasNameEqualTo(String name) {
        return this.value.equals(name);
    }

    @Nonnull
    public static IssueType getByName(@Nonnull String name) {
        IssueType type = NAME_TO_ISSUE_TYPE.get(name);
        if (type == null) {
            throw new IllegalArgumentException("Unknown IssueType: " + name);
        }
        return type;
    }
}
