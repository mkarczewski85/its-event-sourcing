package com.karczewski.its.es.app.service.command;

import com.karczewski.its.es.app.domain.command.AssignIssueCommand;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.command.CommandHandler;
import jakarta.annotation.Nonnull;

public class AssignIssueCommandHandler implements CommandHandler<AssignIssueCommand> {
    @Override
    public void handle(Aggregate aggregate, Command command) {
        aggregate.process(command);
    }

    @Nonnull
    @Override
    public Class<AssignIssueCommand> getCommandType() {
        return AssignIssueCommand.class;
    }
}
