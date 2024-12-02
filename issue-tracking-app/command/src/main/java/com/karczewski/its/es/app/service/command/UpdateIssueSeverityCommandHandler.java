package com.karczewski.its.es.app.service.command;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.command.CommandHandler;
import com.karczewski.its.es.app.domain.command.UpdateIssueSeverityCommand;
import jakarta.annotation.Nonnull;

public class UpdateIssueSeverityCommandHandler implements CommandHandler<UpdateIssueSeverityCommand> {
    @Override
    public void handle(Aggregate aggregate, Command command) {
        aggregate.process(command);
    }

    @Nonnull
    @Override
    public Class<UpdateIssueSeverityCommand> getCommandType() {
        return UpdateIssueSeverityCommand.class;
    }
}
