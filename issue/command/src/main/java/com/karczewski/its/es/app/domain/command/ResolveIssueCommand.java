package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.app.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.command.Command;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class ResolveIssueCommand extends Command {

    private final UUID resolvedBy;

    public ResolveIssueCommand(UUID aggregateId, UUID resolvedBy) {
        super(AggregateType.ISSUE.toString(), aggregateId);
        this.resolvedBy = resolvedBy;
    }
}
