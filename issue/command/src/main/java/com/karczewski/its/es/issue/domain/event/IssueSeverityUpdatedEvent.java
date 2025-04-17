package com.karczewski.its.es.issue.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private final UUID updatedBy;

    @Builder
    public IssueSeverityUpdatedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                                     @JsonProperty("version") int version,
                                     @JsonProperty("issueSeverity") String issueSeverity,
                                     @JsonProperty("updatedBy") UUID updatedBy) {
        super(aggregateId, version);
        this.issueSeverity = issueSeverity;
        this.updatedBy = updatedBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
