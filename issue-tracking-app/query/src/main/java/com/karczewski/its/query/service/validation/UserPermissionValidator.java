package com.karczewski.its.query.service.validation;

import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.exception.QueryPermissionDeniedException;
import com.karczewski.its.security.authentication.AuthenticationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPermissionValidator {
    private final AuthenticationClient authenticationClient;

    public void validateUserPermission(IssueProjection issueProjection) {
        UUID userUuid = authenticationClient.getLoggedUserUuid();
        if (userUuid.equals(issueProjection.getReportedBy()) || userUuid.equals(issueProjection.getAssignedTo())) return;
        throw new QueryPermissionDeniedException("No permission to query issue");
    }
}
