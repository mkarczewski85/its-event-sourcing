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
                .version(getNextVersion())
                .issueTitle(command.getIssueTitle())
                .issueDescription(command.getIssueDescription())
                .issueSeverity(command.getIssueSeverity())
                .issueType(command.getIssueType())
                .reportedBy(command.getReportedBy())
                .build());
    }

    public void process(ReassignIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.ASSIGNED)) {
            applyChange(IssueReassignedEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getNextVersion())
                    .assignedTo(command.getAssignedTo())
                    .assignedBy(command.getAssignedBy())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be reassigned.");
        }
    }

    public void process(AssignIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.ASSIGNED)) {
            applyChange(IssueAssignedEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getNextVersion())
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
                    .version(getNextVersion())
                    .acceptedBy(command.getAcceptedBy())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be accepted.");
        }
    }

    public void process(RejectIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.REJECTED)) {
            applyChange(IssueRejectedEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getNextVersion())
                    .rejectedBy(command.getRejectedBy())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be rejected.");
        }
    }

    public void process(CancelIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.CANCELLED)) {
            applyChange(IssueCancelledEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getNextVersion())
                    .cancelledBy(command.getCancelledBy())
                    .build());
        } else {
            throw new AggregateStateException("This issue cannot be cancelled.");
        }
    }

    public void process(ResolveIssueCommand command) {
        if (this.issueStatus != null && this.issueStatus.canTransitionTo(IssueStatus.RESOLVED)) {
            applyChange(IssueResolvedEvent.builder()
                    .aggregateId(aggregateId)
                    .version(getNextVersion())
                    .resolvedBy(command.getResolvedBy())
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
                .version(getNextVersion())
                .issueSeverity(command.getIssueSeverity())
                .updatedBy(command.getUpdatedBy())
                .build());
    }

    public void process(UpdateIssueTypeCommand command) {
        if (isIssueUnassigned()) {
            throw new AggregateStateException("This issue cannot be updated.");
        }
        if (this.issueType.hasNameEqualTo(command.getIssueType())) return;
        applyChange(IssueTypeUpdatedEvent.builder()
                .aggregateId(aggregateId)
                .version(getNextVersion())
                .issueType(command.getIssueType())
                .updatedBy(command.getUpdatedBy())
                .build());
    }

    public void process(CommentIssueCommand command) {
        if (isIssueCompleted()) {
            throw new AggregateStateException("This issue cannot be commented anymore.");
        }
        applyChange(IssueCommentedEvent.builder()
                .aggregateId(aggregateId)
                .version(getNextVersion())
                .authoredBy(command.getAuthoredBy())
                .comment(command.getComment())
                .build());
    }

    public void apply(IssueReportedEvent event) {
        this.issueTitle = new IssueTitle(event.getIssueTitle());
        this.issueDescription = new IssueDescription(event.getIssueDescription());
        this.reportedBy = new UserId(event.getReportedBy());
        this.issueStatus = IssueStatus.REPORTED;
        this.issueSeverity = IssueSeverity.getByName(event.getIssueSeverity());
        this.issueType = IssueType.getByName(event.getIssueType());
        this.reportedAt = event.getCreatedDate();
        this.numberOfComments = 0;
    }

    public void apply(IssueReassignedEvent event) {
        this.assignedTo = new UserId(event.getAssignedTo());
        this.assignedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void apply(IssueAssignedEvent event) {
        this.issueStatus = IssueStatus.ASSIGNED;
        this.assignedTo = new UserId(event.getAssignedTo());
        this.assignedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void apply(IssueAcceptedEvent event) {
        this.issueStatus = IssueStatus.IN_PROGRESS;
        this.acceptedAt = event.getCreatedDate();
        this.updatedAt = LocalDateTime.now();
    }

    public void apply(IssueRejectedEvent event) {
        this.issueStatus = IssueStatus.REJECTED;
        this.completedAt = event.getCreatedDate();
        this.updatedAt = LocalDateTime.now();
    }

    public void apply(IssueCancelledEvent event) {
        this.issueStatus = IssueStatus.CANCELLED;
        this.completedAt = event.getCreatedDate();
        this.updatedAt = LocalDateTime.now();
    }

    public void apply(IssueResolvedEvent event) {
        this.issueStatus = IssueStatus.RESOLVED;
        this.completedAt = event.getCreatedDate();
        this.updatedAt = LocalDateTime.now();
    }

    public void apply(IssueSeverityUpdatedEvent event) {
        this.issueSeverity = IssueSeverity.getByName(event.getIssueSeverity());
        this.updatedAt = event.getCreatedDate();
    }

    public void apply(IssueTypeUpdatedEvent event) {
        this.issueType = IssueType.getByName(event.getIssueType());
        this.updatedAt = event.getCreatedDate();
        this.updatedAt = LocalDateTime.now();
    }

    public void apply(IssueCommentedEvent event) {
        this.numberOfComments++;
    }

    public UUID getAssignedTo() {
        return this.assignedTo != null ? this.assignedTo.getId() : null;
    }

    public UUID getReportedBy() {
        return this.reportedBy != null ? this.reportedBy.getId() : null;
    }

    private boolean isIssueCompleted() {
        return this.completedAt != null;
    }

    private boolean isIssueUnassigned() {
        return this.assignedAt == null;
    }

    public boolean isAssignedTo(@NonNull UUID uuid) {
        return uuid.equals(this.assignedTo.getId());
    }

    public boolean isReportedBy(@NonNull UUID uuid) {
        return uuid.equals(this.reportedBy.getId());
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return AggregateType.getByAggregateClass(getClass()).toString();
    }

}
