package com.karczewski.its.es.app.service.command;

import com.karczewski.its.es.app.domain.command.ResolveIssueCommand;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.command.CommandHandler;
import jakarta.annotation.Nonnull;

public class ResolveIssueCommandHandler implements CommandHandler<ResolveIssueCommand> {
    @Override
    public void handle(Aggregate aggregate, Command command) {
        aggregate.process(command);
    }

    @Nonnull
    @Override
    public Class<ResolveIssueCommand> getCommandType() {
        return ResolveIssueCommand.class;
    }
}
