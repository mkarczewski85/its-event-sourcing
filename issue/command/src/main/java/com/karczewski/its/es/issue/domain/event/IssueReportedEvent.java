package com.karczewski.its.es.issue.domain.event;

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
public class IssueReportedEvent extends Event {

    private final String issueTitle;
    private final String issueDescription;
    private final String issueSeverity;
    private final String issueType;
    private final UUID reportedBy;

    @JsonCreator
    @Builder
    public IssueReportedEvent(@JsonProperty("aggregateId") UUID aggregateId,
                              @JsonProperty("version") int version,
                              @JsonProperty("issueTitle") String issueTitle,
                              @JsonProperty("issueDescription") String issueDescription,
                              @JsonProperty("issueSeverity") String issueSeverity,
                              @JsonProperty("issueType") String issueType,
                              @JsonProperty("reportedBy") UUID reportedBy) {
        super(aggregateId, version);
        this.issueTitle = issueTitle;
        this.issueDescription = issueDescription;
        this.issueSeverity = issueSeverity;
        this.issueType = issueType;
        this.reportedBy = reportedBy;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.getByEventClass(getClass()).toString();
    }
}
