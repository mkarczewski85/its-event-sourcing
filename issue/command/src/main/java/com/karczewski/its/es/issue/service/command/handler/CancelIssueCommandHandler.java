package com.karczewski.its.es.issue.service.command.handler;

import com.karczewski.its.es.issue.domain.command.CancelIssueCommand;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.command.CommandHandler;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CancelIssueCommandHandler implements CommandHandler<CancelIssueCommand> {
    @Override
    public void handle(Aggregate aggregate, Command command) {
        aggregate.processCommand(command);
    }

    @Nonnull
    @Override
    public Class<CancelIssueCommand> getCommandType() {
        return CancelIssueCommand.class;
    }
}
