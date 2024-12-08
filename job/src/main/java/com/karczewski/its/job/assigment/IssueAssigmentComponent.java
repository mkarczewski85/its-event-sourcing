package com.karczewski.its.job.assigment;

import com.karczewski.its.query.IssueProjectionQueryClient;
import com.karczewski.its.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssueAssigmentComponent {

    private final IssueProjectionQueryClient issueQueryClient;
    private final UserClient userClient;

}
