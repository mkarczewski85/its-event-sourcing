package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
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
        super(IssueAggregate.AGGREGATE_TYPE_NAME, aggregateId);
        this.acceptedBy = acceptedBy;
    }
}
