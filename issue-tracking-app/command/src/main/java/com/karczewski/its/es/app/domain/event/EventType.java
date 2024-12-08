package com.karczewski.its.es.app.domain.event;

import com.karczewski.its.es.core.domain.event.Event;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType {

    ISSUE_REPORTED(IssueReportedEvent.class),
    ISSUE_ASSIGNED(IssueAssignedEvent.class),
    ISSUE_ACCEPTED(IssueAcceptedEvent.class),
    ISSUE_REJECTED(IssueRejectedEvent.class),
    ISSUE_REASSIGNED(IssueReassignedEvent.class),
    ISSUE_CANCELLED(IssueCancelledEvent.class),
    ISSUE_RESOLVED(IssueResolvedEvent.class),
    ISSUE_COMMENTED(IssueCommentedEvent.class),
    ISSUE_SEVERITY_UPDATED(IssueSeverityUpdatedEvent.class),
    ISSUE_TYPE_UPDATED(IssueTypeUpdatedEvent.class);

    private final Class<? extends Event> eventClass;

    public static EventType getByEventClass(Class<? extends Event> clazz) {
        for (EventType eventType : EventType.values()) {
            if (eventType.getEventClass().equals(clazz)) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("No EventType found for class: " + clazz.getName());
    }

}
