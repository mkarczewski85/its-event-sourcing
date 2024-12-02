package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class CancelIssueCommand extends Command {

    // TODO

    public CancelIssueCommand(String aggregateType, UUID aggregateId) {
        super(aggregateType, aggregateId);
    }
}
