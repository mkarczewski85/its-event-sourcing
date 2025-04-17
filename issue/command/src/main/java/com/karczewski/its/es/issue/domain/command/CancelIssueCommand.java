package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.issue.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.command.Command;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class CancelIssueCommand extends Command {

    private final UUID cancelledBy;

    public CancelIssueCommand(UUID aggregateId, UUID cancelledBy) {
        super(AggregateType.ISSUE.toString(), aggregateId);
        this.cancelledBy = cancelledBy;
    }
}
