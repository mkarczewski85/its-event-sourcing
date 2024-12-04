package com.karczewski.its.query;

import com.karczewski.its.query.entity.IssueComment;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.service.filters.IssueFilters;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IssueProjectionQueryClient {

    IssueProjection getIssueProjection(UUID uuid);

    Page<IssueProjection> getAssignedIssues(IssueFilters filters, int offset, int limit);

    Page<IssueProjection> getReportedIssues(IssueFilters filters, int offset, int limit);

    Page<IssueComment> getIssueComments(UUID uuid, int offset, int limit);

}
