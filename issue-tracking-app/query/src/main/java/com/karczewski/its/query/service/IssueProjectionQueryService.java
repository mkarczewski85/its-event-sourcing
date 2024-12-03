package com.karczewski.its.query.service;

import com.karczewski.its.query.IssueProjectionQueryClient;
import com.karczewski.its.query.dto.IssueCommentDto;
import com.karczewski.its.query.dto.IssueProjectionDisplayItemDto;
import com.karczewski.its.query.dto.pagination.IssueFilters;
import com.karczewski.its.query.dto.pagination.PageWrapper;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.exception.IssueNotFoundException;
import com.karczewski.its.query.repository.IssueProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueProjectionQueryService implements IssueProjectionQueryClient {

    private final IssueProjectionRepository issueProjectionRepository;

    @Override
    public IssueProjection getIssueProjection(UUID uuid) {
        return issueProjectionRepository.findByUuid(uuid)
                .orElseThrow(() -> new IssueNotFoundException("Unable to find issue with uuid: " + uuid));
    }

    @Override
    public PageWrapper<IssueProjectionDisplayItemDto> getAssignedIssues(IssueFilters filters, int offset, int limit) {
        // TODO
        return null;
    }

    @Override
    public PageWrapper<IssueProjectionDisplayItemDto> getReportedIssues(IssueFilters filters, int offset, int limit) {
        // TODO
        return null;
    }

    @Override
    public PageWrapper<IssueCommentDto> getIssueComments(UUID uuid, int offset, int limit) {
        // TODO
        return null;
    }
}
