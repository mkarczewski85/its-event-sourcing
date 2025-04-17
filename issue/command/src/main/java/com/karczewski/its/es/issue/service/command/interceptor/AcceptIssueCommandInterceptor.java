package com.karczewski.its.es.issue.service.command.interceptor;

import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
import com.karczewski.its.es.issue.domain.command.AcceptIssueCommand;
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
public class AcceptIssueCommandInterceptor implements CommandInterceptor<AcceptIssueCommand> {

    @Override
    public void intercept(Aggregate aggregate, Command command) {
        IssueAggregate issueAggregate = CastingUtility.safeCast(aggregate, IssueAggregate.class);
        AcceptIssueCommand acceptIssueCommand = CastingUtility.safeCast(command, AcceptIssueCommand.class);
        if (issueAggregate.isAssignedTo(acceptIssueCommand.getAcceptedBy())) return;
        throw new AggregatePermissionDeniedException("No permission to accept the issue.");
    }

    @Nonnull
    @Override
    public Class<AcceptIssueCommand> getCommandType() {
        return AcceptIssueCommand.class;
    }
}
