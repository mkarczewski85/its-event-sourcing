package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.app.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.command.Command;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class UpdateIssueTypeCommand extends Command {

    private final String issueType;
    private final UUID updatedBy;

    @Builder
    public UpdateIssueTypeCommand(UUID aggregateId, String issueType, UUID updatedBy) {
        super(AggregateType.ISSUE.toString(), aggregateId);
        this.issueType = issueType;
        this.updatedBy = updatedBy;
    }
}
