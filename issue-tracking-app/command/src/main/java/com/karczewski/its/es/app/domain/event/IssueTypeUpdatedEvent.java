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
    private final UUID updatedBy;

    @Builder
    public IssueTypeUpdatedEvent(UUID aggregateId, int version, String issueType, UUID updatedBy) {
        super(aggregateId, version);
        this.issueType = issueType;
        this.updatedBy = updatedBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
