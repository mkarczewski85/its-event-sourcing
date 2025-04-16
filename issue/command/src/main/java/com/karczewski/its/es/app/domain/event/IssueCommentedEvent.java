package com.karczewski.its.es.app.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.karczewski.its.es.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class IssueCommentedEvent extends Event {

    @JsonIgnore
    private final String comment;
    private final UUID authoredBy;

    @JsonCreator
    @Builder
    public IssueCommentedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                               @JsonProperty("version") int version,
                               @JsonProperty("comment") String comment,
                               @JsonProperty("authoredBy") UUID authoredBy) {
        super(aggregateId, version);
        this.comment = comment;
        this.authoredBy = authoredBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
