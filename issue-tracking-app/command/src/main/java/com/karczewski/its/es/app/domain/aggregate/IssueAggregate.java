package com.karczewski.its.es.app.domain.aggregate;

import com.karczewski.its.es.app.domain.aggregate.valueobject.*;
import com.karczewski.its.es.app.domain.command.*;
import com.karczewski.its.es.app.domain.event.*;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.exception.AggregateStateException;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class IssueAggregate extends Aggregate {

    private IssueTitle issueTitle;
    private IssueDescription issueDescription;
    private IssueStatus issueStatus;
    private IssueSeverity issueSeverity;
    private IssueType issueType;
    private UserId reportedBy;
    private UserId assignedTo;
    private LocalDateTime reportedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
    private int numberOfComments;

    public IssueAggregate(@NonNull UUID aggregateId, int version) {
        super(aggregateId, version);
    }

    public void process(ReportIssueCommand command) {
        if (issueStatus != null) {
            throw new AggregateStateException("This issue is already reported.");
        }
        applyChange(IssueReportedEvent.builder()
                .aggregateId(aggregateId)
                .version(getVersion())
                .issueTitle(command.getIssueTitle())
                .issueDescription(command.getIssueDescription())
                .issueSeverity(command.getIssueSeverity())
                .issueType(command.getIssueType())
                .reportedBy(command.getReportedBy())
                .build());
    }

    public void process(AssignIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.ASSIGNED)) {
            applyChange(IssueAssignedEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getVersion())
                    .assignedTo(command.getAssignedTo())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be assigned.");
        }
    }

    public void process(AcceptIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.IN_PROGRESS)) {
            applyChange(IssueAcceptedEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getVersion())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be accepted.");
        }
    }

    public void process(RejectIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.REJECTED)) {
            applyChange(IssueRejectedEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getVersion())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be rejected.");
        }
    }

    public void process(CancelIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.CANCELLED)) {
            applyChange(IssueCancelledEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getVersion())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be cancelled.");
        }
    }

    public void process(ResolveIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.RESOLVED)) {
            applyChange(IssueResolvedEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getVersion())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be resolved.");
        }
    }

    public void process(UpdateIssueSeverityCommand command) {
        if (isIssueUnassigned()) {
            throw new AggregateStateException("This issue cannot be updated.");
        }
        if (this.issueSeverity.hasNameEqualTo(command.getIssueSeverity())) return;
        applyChange(IssueSeverityUpdatedEvent.builder()
                .aggregateId(aggregateId)
                .version(getVersion())
                .issueSeverity(command.getIssueSeverity())
                .build());
    }

    public void process(UpdateIssueTypeCommand command) {
        if (isIssueUnassigned()) {
            throw new AggregateStateException("This issue cannot be updated.");
        }
        if (this.issueType.hasNameEqualTo(command.getIssueType())) return;
        applyChange(IssueTypeUpdatedEvent.builder()
                .aggregateId(aggregateId)
                .version(getVersion())
                .issueType(command.getIssueType())
                .build());
    }

    public void process(CommentIssueCommand command) {
        if (isIssueCompleted()) {
            throw new AggregateStateException("This issue cannot be commented anymore.");
        }
        applyChange(IssueCommentedEvent.builder()
                .aggregateId(aggregateId)
                .version(getVersion())
                .authoredBy(command.getAuthoredBy())
                .comment(command.getComment())
                .build());
    }

    private void apply(IssueReportedEvent event) {
        this.issueTitle = new IssueTitle(event.getIssueTitle());
        this.issueDescription = new IssueDescription(event.getIssueDescription());
        this.reportedBy = new UserId(event.getReportedBy());
        this.issueStatus = IssueStatus.REPORTED;
        this.issueSeverity = IssueSeverity.getByName(event.getIssueSeverity());
        this.issueType = IssueType.getByName(event.getEventType());
        this.reportedAt = event.getCreatedDate();
        this.numberOfComments = 0;
    }

    private void apply(IssueAssignedEvent event) {
        this.assignedTo = new UserId(event.getAssignedTo());
    }

    private void apply(IssueAcceptedEvent event) {
        this.issueStatus = IssueStatus.IN_PROGRESS;
        this.acceptedAt = event.getCreatedDate();
    }

    private void apply(IssueRejectedEvent event) {
        this.issueStatus = IssueStatus.REJECTED;
        this.completedAt = event.getCreatedDate();
    }

    private void apply(IssueCancelledEvent event) {
        this.issueStatus = IssueStatus.CANCELLED;
        this.completedAt = event.getCreatedDate();
    }

    private void apply(IssueResolvedEvent event) {
        this.issueStatus = IssueStatus.RESOLVED;
        this.completedAt = event.getCreatedDate();
    }

    private void apply(IssueSeverityUpdatedEvent event) {
        this.issueSeverity = IssueSeverity.getByName(event.getIssueSeverity());
        this.updatedAt = event.getCreatedDate();
    }

    private void apply(IssueTypeUpdatedEvent event) {
        this.issueType = IssueType.getByName(event.getIssueType());
        this.updatedAt = event.getCreatedDate();
    }

    private void apply(IssueCommentedEvent event) {
        this.numberOfComments++;
    }

    private boolean isIssueCompleted() {
        return this.completedAt != null;
    }

    private boolean isIssueUnassigned() {
        return this.assignedAt == null;
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return AggregateType.getByAggregateClass(getClass()).toString();
    }

}
