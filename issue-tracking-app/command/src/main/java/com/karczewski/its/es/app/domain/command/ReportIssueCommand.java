package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
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
    private final String reportedBy;

    @Builder
    public ReportIssueCommand(String aggregateType,
                              UUID aggregateId,
                              String issueTitle,
                              String issueDescription,
                              String issueSeverity,
                              String issueType,
                              String reportedBy) {
        super(aggregateType, aggregateId);
        this.issueTitle = issueTitle;
        this.issueDescription = issueDescription;
        this.issueSeverity = issueSeverity;
        this.issueType = issueType;
        this.reportedBy = reportedBy;
    }

    public UUID getReportedBy() {
        return UUID.fromString(reportedBy);
    }
}
