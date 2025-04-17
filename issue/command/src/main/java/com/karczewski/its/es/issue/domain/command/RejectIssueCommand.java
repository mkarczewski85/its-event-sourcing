package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class RejectIssueCommand extends Command {

    private final UUID rejectedBy;

    public RejectIssueCommand(UUID aggregateId, UUID rejectedBy) {
        super(IssueAggregate.AGGREGATE_TYPE_NAME, aggregateId);
        this.rejectedBy = rejectedBy;
    }
}
