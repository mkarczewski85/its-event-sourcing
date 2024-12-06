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
public class IssueRejectedEvent extends Event {

    private final UUID rejectedBy;

    @JsonCreator
    @Builder
    public IssueRejectedEvent(UUID aggregateId, int version, UUID rejectedBy) {
        super(aggregateId, version);
        this.rejectedBy = rejectedBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
