package com.karczewski.its.es.issue.service.command.handler;

import com.karczewski.its.es.issue.domain.command.AcceptIssueCommand;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.command.CommandHandler;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AcceptIssueCommandHandler implements CommandHandler<AcceptIssueCommand> {
    @Override
    public void handle(Aggregate aggregate, Command command) {
        aggregate.processCommand(command);
    }

    @Nonnull
    @Override
    public Class<AcceptIssueCommand> getCommandType() {
        return AcceptIssueCommand.class;
    }
}
