package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class AssignIssueCommand extends Command {

    private final UUID assignedTo;

    @Builder
    public AssignIssueCommand(UUID aggregateId, UUID assignedTo) {
        super(IssueAggregate.AGGREGATE_TYPE_NAME, aggregateId);
        this.assignedTo = assignedTo;
    }
}
