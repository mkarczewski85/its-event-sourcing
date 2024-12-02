package com.karczewski.its.es.app.domain.aggregate.valueobject;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class IssueStatus {

    private final String value;

    public static final IssueStatus REPORTED = new IssueStatus("REPORTED");
    public static final IssueStatus ASSIGNED = new IssueStatus("ASSIGNED");
    public static final IssueStatus IN_PROGRESS = new IssueStatus("IN_PROGRESS");
    public static final IssueStatus REJECTED = new IssueStatus("REJECTED");
    public static final IssueStatus CANCELLED = new IssueStatus("CANCELLED");
    public static final IssueStatus RESOLVED = new IssueStatus("RESOLVED");

    private static final Map<String, IssueStatus> NAME_TO_ISSUE_STATUS = Map.of(
            REPORTED.getValue(), REPORTED,
            ASSIGNED.getValue(), ASSIGNED,
            IN_PROGRESS.getValue(), IN_PROGRESS,
            REJECTED.getValue(), REJECTED,
            CANCELLED.getValue(), CANCELLED,
            RESOLVED.getValue(), RESOLVED
    );

    @Nonnull
    public static IssueStatus getByName(@Nonnull String name) {
        IssueStatus status = NAME_TO_ISSUE_STATUS.get(name);
        if (status == null) {
            throw new IllegalArgumentException("Unknown IssueStatus level: " + name);
        }
        return status;
    }

    public boolean canTransitionTo(IssueStatus nextStatus) {
        if (this.equals(REPORTED) && nextStatus.equals(ASSIGNED)) return true;
        if (this.equals(ASSIGNED) && (nextStatus.equals(IN_PROGRESS) || nextStatus.equals(REJECTED) || nextStatus.equals(ASSIGNED))) return true;
        return this.equals(IN_PROGRESS) && (nextStatus.equals(RESOLVED) || nextStatus.equals(CANCELLED) || nextStatus.equals(ASSIGNED));
    }

}
