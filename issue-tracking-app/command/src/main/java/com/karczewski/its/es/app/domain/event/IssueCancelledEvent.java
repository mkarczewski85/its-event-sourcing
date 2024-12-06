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
public class IssueCancelledEvent extends Event {

    private final UUID cancelledBy;

    @JsonCreator
    @Builder
    public IssueCancelledEvent(UUID aggregateId, int version, UUID cancelledBy) {
        super(aggregateId, version);
        this.cancelledBy = cancelledBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
