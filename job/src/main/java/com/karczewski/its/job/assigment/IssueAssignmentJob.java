package com.karczewski.its.job.assigment;

import com.karczewski.its.job.assigment.utility.CyclicIterator;
import com.karczewski.its.query.IssueProjectionQueryClient;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.entity.UserIssueCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class IssueAssignmentJob {

    private final IssueAssigmentComponent issueAssigmentComponent;
    private final IssueProjectionQueryClient queryClient;

    @Scheduled(
            fixedDelayString = "${issues.assigment.polling-interval}",
            initialDelayString = "${issues.assigment.polling-initial-delay}"
    )
    @SchedulerLock(
            name = "IssueAssignmentTaskLock",
            lockAtMostFor = "${issues.assigment.polling-interval}",
            lockAtLeastFor = "${issues.assigment.polling-interval}"
    )
    public void assignReportedIssues() {
        Collection<IssueProjection> allUnassigned = queryClient.getAllUnassignedIssues();
        if (allUnassigned.isEmpty()) {
            log.debug("No unassigned issues found.");
            return;
        }
        List<UserIssueCount> userAssignmentsCount = queryClient.getUserAssignmentsCount(allUnassigned.size());
        Iterator<UserIssueCount> cyclicIterator = new CyclicIterator<>(userAssignmentsCount);
        allUnassigned.stream()
                .map(IssueProjection::getId)
                .forEach(id -> issueAssigmentComponent.assignIssue(id, cyclicIterator.next().getUserId()));
    }

}
