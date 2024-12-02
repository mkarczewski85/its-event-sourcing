package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class UpdateIssueTypeCommand extends Command {

    private final String issueType;

    @Builder
    public UpdateIssueTypeCommand(String aggregateType, UUID aggregateId, String issueType) {
        super(aggregateType, aggregateId);
        this.issueType = issueType;
    }
}
