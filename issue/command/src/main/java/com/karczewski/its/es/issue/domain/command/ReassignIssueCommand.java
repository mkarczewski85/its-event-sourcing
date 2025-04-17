package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class ReassignIssueCommand extends Command {

    private final UUID assignedTo;
    private final UUID assignedBy;

    @Builder
    public ReassignIssueCommand(UUID aggregateId, UUID assignedTo, UUID assignedBy) {
        super(IssueAggregate.AGGREGATE_TYPE_NAME, aggregateId);
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
    }
}
