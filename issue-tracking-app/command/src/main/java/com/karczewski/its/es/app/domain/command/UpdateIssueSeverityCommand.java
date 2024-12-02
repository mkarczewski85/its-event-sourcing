package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class UpdateIssueSeverityCommand extends Command {

    private final String issueSeverity;

    @Builder
    public UpdateIssueSeverityCommand(String aggregateType, UUID aggregateId, String issueSeverity) {
        super(aggregateType, aggregateId);
        this.issueSeverity = issueSeverity;
    }
}
