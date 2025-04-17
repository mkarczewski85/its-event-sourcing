package com.karczewski.its.es.issue.service.command.interceptor;

import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
import com.karczewski.its.es.issue.domain.command.CancelIssueCommand;
import com.karczewski.its.es.issue.service.utility.CastingUtility;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.exception.AggregatePermissionDeniedException;
import com.karczewski.its.es.core.service.command.CommandInterceptor;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CancelIssueCommandInterceptor implements CommandInterceptor<CancelIssueCommand> {
    @Override
    public void intercept(Aggregate aggregate, Command command) {
        IssueAggregate issueAggregate = CastingUtility.safeCast(aggregate, IssueAggregate.class);
        CancelIssueCommand acceptIssueCommand = CastingUtility.safeCast(command, CancelIssueCommand.class);
        if (issueAggregate.isAssignedTo(acceptIssueCommand.getCancelledBy())) return;
        throw new AggregatePermissionDeniedException("No permission to cancel the issue.");
    }

    @Nonnull
    @Override
    public Class<CancelIssueCommand> getCommandType() {
        return CancelIssueCommand.class;
    }
}
