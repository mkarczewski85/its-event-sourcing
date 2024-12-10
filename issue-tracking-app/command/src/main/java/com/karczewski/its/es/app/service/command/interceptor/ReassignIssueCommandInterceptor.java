package com.karczewski.its.es.app.service.command.interceptor;

import com.karczewski.its.es.app.domain.aggregate.IssueAggregate;
import com.karczewski.its.es.app.domain.command.ReassignIssueCommand;
import com.karczewski.its.es.app.service.utility.CastingUtility;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.exception.AggregatePermissionDeniedException;
import com.karczewski.its.es.core.service.command.CommandInterceptor;
import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.entity.UserRole;
import com.karczewski.its.user.exception.UserAccountNotFoundException;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReassignIssueCommandInterceptor implements CommandInterceptor<ReassignIssueCommand> {

    private final UserClient userClient;

    @Override
    public void intercept(Aggregate aggregate, Command command) {
        IssueAggregate issueAggregate = CastingUtility.safeCast(aggregate, IssueAggregate.class);
        ReassignIssueCommand reassignIssueCommand = CastingUtility.safeCast(command, ReassignIssueCommand.class);
        if (!issueAggregate.isAssignedTo(reassignIssueCommand.getAssignedBy())) {
            throw new AggregatePermissionDeniedException("No permission to accept the issue.");
        }
        if (!userClient.existsByUUIDAndRole(reassignIssueCommand.getAssignedTo(), UserRole.TECHNICIAN)) {
            throw new UserAccountNotFoundException("Unable to find assigned user");
        }
    }

    @Nonnull
    @Override
    public Class<ReassignIssueCommand> getCommandType() {
        return ReassignIssueCommand.class;
    }
}
