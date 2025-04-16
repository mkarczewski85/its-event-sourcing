package com.karczewski.its.query.service.validation;

import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.exception.QueryPermissionDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPermissionValidator {

    public void validateUserPermission(IssueProjection issueProjection, UUID userId) {
        if (issueProjection.hasPermissionToQuery(userId)) return;
        throw new QueryPermissionDeniedException("No permission to query issue");
    }
}
