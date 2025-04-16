package com.karczewski.its.es.app.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.karczewski.its.es.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class IssueReassignedEvent extends Event {

    private final UUID assignedTo;
    private final UUID assignedBy;

    @JsonCreator
    @Builder
    public IssueReassignedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                                @JsonProperty("version") int version,
                                @JsonProperty("assignedTo") UUID assignedTo,
                                @JsonProperty("assignedBy") UUID assignedBy) {
        super(aggregateId, version);
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
