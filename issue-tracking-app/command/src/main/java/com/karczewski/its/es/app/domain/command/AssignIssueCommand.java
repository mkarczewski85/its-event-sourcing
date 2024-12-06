package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.app.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.command.Command;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class AssignIssueCommand extends Command {

    private final UUID assignedTo;
    private final UUID assignedBy;

    @Builder
    public AssignIssueCommand(UUID aggregateId, UUID assignedTo, UUID assignedBy) {
        super(AggregateType.ISSUE.toString(), aggregateId);
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
    }
}
