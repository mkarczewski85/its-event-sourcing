package com.karczewski.its.job.assigment;

import com.karczewski.its.es.core.service.CommandProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueAssignmentJob {

    private final CommandProcessor commandProcessor;

    @Scheduled(fixedDelayString = "${issues.assigment.interval}", initialDelayString = "${issues.assigment.interval}")
    public void assignReportedIssues() {

    }

}
