package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class ReportIssueCommand extends Command {

    private final String issueTitle;
    private final String issueDescription;
    private final String issueSeverity;
    private final String issueType;
    private final UUID reportedBy;

    @Builder
    public ReportIssueCommand(String issueTitle,
                              String issueDescription,
                              String issueSeverity,
                              String issueType,
                              UUID reportedBy) {
        super(IssueAggregate.AGGREGATE_TYPE_NAME, generateAggregateId());
        this.issueTitle = issueTitle;
        this.issueDescription = issueDescription;
        this.issueSeverity = issueSeverity;
        this.issueType = issueType;
        this.reportedBy = reportedBy;
    }

    private static UUID generateAggregateId() {
        return UUID.randomUUID();
    }
}
