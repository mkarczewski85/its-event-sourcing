package com.karczewski.its.query;

import com.karczewski.its.query.dto.IssueCommentDto;
import com.karczewski.its.query.dto.IssueProjectionDisplayItemDto;
import com.karczewski.its.query.dto.pagination.IssueFilters;
import com.karczewski.its.query.dto.pagination.PageWrapper;
import com.karczewski.its.query.entity.IssueProjection;

import java.util.UUID;

public interface IssueProjectionQueryClient {

    IssueProjection getIssueProjection(UUID uuid);

    PageWrapper<IssueProjectionDisplayItemDto> getAssignedIssues(IssueFilters filters, int offset, int limit);

    PageWrapper<IssueProjectionDisplayItemDto> getReportedIssues(IssueFilters filters, int offset, int limit);

    PageWrapper<IssueCommentDto> getIssueComments(UUID uuid, int offset, int limit);

}
