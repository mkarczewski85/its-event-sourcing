package com.karczewski.its.es.app.service.command.handler;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.command.CommandHandler;
import com.karczewski.its.es.app.domain.command.ReportIssueCommand;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportIssueCommandHandler implements CommandHandler<ReportIssueCommand> {
    @Override
    public void handle(Aggregate aggregate, Command command) {
        aggregate.processCommand(command);
    }

    @Nonnull
    @Override
    public Class<ReportIssueCommand> getCommandType() {
        return ReportIssueCommand.class;
    }
}
