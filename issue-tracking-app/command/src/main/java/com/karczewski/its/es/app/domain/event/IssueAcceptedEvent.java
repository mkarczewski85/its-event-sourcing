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
public class IssueAcceptedEvent extends Event {


    @JsonCreator
    @Builder
    public IssueAcceptedEvent(UUID aggregateId, int version) {
        super(aggregateId, version);
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
