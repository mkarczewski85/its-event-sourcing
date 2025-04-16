package com.karczewski.its.es.app.service.command.interceptor;

import com.karczewski.its.es.app.domain.aggregate.IssueAggregate;
import com.karczewski.its.es.app.domain.command.CommentIssueCommand;
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
public class CommentIssueCommandInterceptor implements CommandInterceptor<CommentIssueCommand> {
    @Override
    public void intercept(Aggregate aggregate, Command command) {
        IssueAggregate issueAggregate = CastingUtility.safeCast(aggregate, IssueAggregate.class);
        CommentIssueCommand commentIssueCommand = CastingUtility.safeCast(command, CommentIssueCommand.class);
        if (issueAggregate.isAssignedTo(commentIssueCommand.getAuthoredBy()) ||
                issueAggregate.isReportedBy(commentIssueCommand.getAuthoredBy())) return;
        throw new AggregatePermissionDeniedException("No permission to reject the issue.");
    }

    @Nonnull
    @Override
    public Class<CommentIssueCommand> getCommandType() {
        return CommentIssueCommand.class;
    }
}
