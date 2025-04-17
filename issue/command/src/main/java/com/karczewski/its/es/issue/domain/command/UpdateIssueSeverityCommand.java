package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.issue.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.command.Command;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class UpdateIssueSeverityCommand extends Command {

    private final String issueSeverity;
    private final UUID updatedBy;

    @Builder
    public UpdateIssueSeverityCommand(UUID aggregateId, String issueSeverity, UUID updatedBy) {
        super(AggregateType.ISSUE.toString(), aggregateId);
        this.issueSeverity = issueSeverity;
        this.updatedBy = updatedBy;
    }
}
