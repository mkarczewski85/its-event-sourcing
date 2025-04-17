package com.karczewski.its.job.assigment;

import com.karczewski.its.es.issue.domain.command.AssignIssueCommand;
import com.karczewski.its.es.core.service.CommandProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IssueAssigmentComponent {

    private final CommandProcessor commandProcessor;

    public void assignIssue(UUID issueId, UUID userId) {
        commandProcessor.process(AssignIssueCommand.builder()
                .aggregateId(issueId)
                .assignedTo(userId)
                .build());
    }

}
