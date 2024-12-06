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
public class IssueResolvedEvent extends Event {

    private final UUID resolvedBy;

    @JsonCreator
    @Builder
    public IssueResolvedEvent(UUID aggregateId, int version, UUID resolvedBy) {
        super(aggregateId, version);
        this.resolvedBy = resolvedBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
