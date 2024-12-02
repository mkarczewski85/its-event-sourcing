package com.karczewski.its.es.app.domain.event;

import com.karczewski.its.es.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class IssueSeverityUpdatedEvent extends Event {

    private final String issueSeverity;

    @Builder
    public IssueSeverityUpdatedEvent(UUID aggregateId, int version, String issueSeverity) {
        super(aggregateId, version);
        this.issueSeverity = issueSeverity;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
