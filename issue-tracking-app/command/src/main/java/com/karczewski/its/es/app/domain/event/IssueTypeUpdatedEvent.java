package com.karczewski.its.es.app.domain.event;

import com.karczewski.its.es.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class IssueTypeUpdatedEvent extends Event {

    private final String issueType;

    @Builder
    public IssueTypeUpdatedEvent(UUID aggregateId, int version, String issueType) {
        super(aggregateId, version);
        this.issueType = issueType;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
