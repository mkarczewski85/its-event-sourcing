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
public class IssueAcceptedEvent extends Event {

    private final UUID acceptedBy;

    @JsonCreator
    @Builder
    public IssueAcceptedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                              @JsonProperty("version") int version,
                              @JsonProperty("acceptedBy") UUID acceptedBy) {
        super(aggregateId, version);
        this.acceptedBy = acceptedBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
