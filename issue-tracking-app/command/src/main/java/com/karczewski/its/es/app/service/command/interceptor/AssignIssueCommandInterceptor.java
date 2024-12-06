package com.karczewski.its.es.app.service.command.interceptor;

import com.karczewski.its.es.app.domain.aggregate.IssueAggregate;
import com.karczewski.its.es.app.domain.command.AssignIssueCommand;
import com.karczewski.its.es.app.service.utility.CastingUtility;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.exception.AggregatePermissionDeniedException;
import com.karczewski.its.es.core.service.command.CommandInterceptor;
import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.entity.UserRole;
import com.karczewski.its.user.exception.UserNotFoundException;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssignIssueCommandInterceptor implements CommandInterceptor<AssignIssueCommand> {

    private final UserClient userClient;

    @Override
    public void intercept(Aggregate aggregate, Command command) {
        IssueAggregate issueAggregate = CastingUtility.safeCast(aggregate, IssueAggregate.class);
        AssignIssueCommand assignIssueCommand = CastingUtility.safeCast(command, AssignIssueCommand.class);
        if (!issueAggregate.isAssignedTo(assignIssueCommand.getAssignedBy())) {
            throw new AggregatePermissionDeniedException("No permission to accept the issue.");
        }
        if (!userClient.existsByUUIDAndRole(assignIssueCommand.getAssignedTo(), UserRole.TECHNICIAN)) {
            throw new UserNotFoundException("Unable to find assigned user");
        }
    }

    @Nonnull
    @Override
    public Class<AssignIssueCommand> getCommandType() {
        return AssignIssueCommand.class;
    }
}
