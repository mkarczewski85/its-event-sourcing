package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class AssignIssueCommand extends Command {

    private final String assignedTo;

    @Builder
    public AssignIssueCommand(String aggregateType, UUID aggregateId, String assignedTo) {
        super(aggregateType, aggregateId);
        this.assignedTo = assignedTo;
    }

    public UUID getAssignedTo() {
        return UUID.fromString(assignedTo);
    }
}
