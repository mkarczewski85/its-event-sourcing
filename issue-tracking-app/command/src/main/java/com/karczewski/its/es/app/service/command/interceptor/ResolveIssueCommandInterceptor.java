package com.karczewski.its.es.app.service.command.interceptor;

import com.karczewski.its.es.app.domain.aggregate.IssueAggregate;
import com.karczewski.its.es.app.domain.command.ResolveIssueCommand;
import com.karczewski.its.es.app.service.utility.CastingUtility;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.exception.AggregatePermissionDeniedException;
import com.karczewski.its.es.core.service.command.CommandInterceptor;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResolveIssueCommandInterceptor implements CommandInterceptor<ResolveIssueCommand> {
    @Override
    public void intercept(Aggregate aggregate, Command command) {
        IssueAggregate issueAggregate = CastingUtility.safeCast(aggregate, IssueAggregate.class);
        ResolveIssueCommand acceptIssueCommand = CastingUtility.safeCast(command, ResolveIssueCommand.class);
        if (issueAggregate.isAssignedTo(acceptIssueCommand.getResolvedBy())) return;
        throw new AggregatePermissionDeniedException("No permission to resolve the issue.");
    }

    @Nonnull
    @Override
    public Class<ResolveIssueCommand> getCommandType() {
        return ResolveIssueCommand.class;
    }
}
