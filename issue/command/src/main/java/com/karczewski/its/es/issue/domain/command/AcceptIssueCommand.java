package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.issue.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.command.Command;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class AcceptIssueCommand extends Command {

    private final UUID acceptedBy;

    @Builder
    public AcceptIssueCommand(UUID aggregateId, UUID acceptedBy) {
        super(AggregateType.ISSUE.toString(), aggregateId);
        this.acceptedBy = acceptedBy;
    }
}
