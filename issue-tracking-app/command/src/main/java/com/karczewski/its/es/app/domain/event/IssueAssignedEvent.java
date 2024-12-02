package com.karczewski.its.es.app.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.karczewski.its.es.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class IssueAssignedEvent extends Event {

    private final UUID assignedTo;

    @JsonCreator
    @Builder
    public IssueAssignedEvent(UUID aggregateId, int version, UUID assignedTo) {
        super(aggregateId, version);
        this.assignedTo = assignedTo;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
