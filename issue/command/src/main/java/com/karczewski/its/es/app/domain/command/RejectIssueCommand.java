package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.app.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.command.Command;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class RejectIssueCommand extends Command {

    private final UUID rejectedBy;

    public RejectIssueCommand(UUID aggregateId, UUID rejectedBy) {
        super(AggregateType.ISSUE.toString(), aggregateId);
        this.rejectedBy = rejectedBy;
    }
}
