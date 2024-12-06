package com.karczewski.its.es.app.service.command;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.command.CommandHandler;
import com.karczewski.its.es.app.domain.command.UpdateIssueTypeCommand;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateIssueTypeCommandHandler implements CommandHandler<UpdateIssueTypeCommand> {
    @Override
    public void handle(Aggregate aggregate, Command command) {
        aggregate.process(command);
    }

    @Nonnull
    @Override
    public Class<UpdateIssueTypeCommand> getCommandType() {
        return UpdateIssueTypeCommand.class;
    }
}
