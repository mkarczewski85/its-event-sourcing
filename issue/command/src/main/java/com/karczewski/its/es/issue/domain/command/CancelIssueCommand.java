package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class CancelIssueCommand extends Command {

    private final UUID cancelledBy;

    public CancelIssueCommand(UUID aggregateId, UUID cancelledBy) {
        super(IssueAggregate.AGGREGATE_TYPE_NAME, aggregateId);
        this.cancelledBy = cancelledBy;
    }
}
