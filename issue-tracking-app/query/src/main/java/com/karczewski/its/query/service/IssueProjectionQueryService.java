package com.karczewski.its.query.service;

import com.karczewski.its.query.IssueProjectionQueryClient;
import com.karczewski.its.query.entity.IssueComment;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.exception.IssueNotFoundException;
import com.karczewski.its.query.service.filters.IssueFilters;
import com.karczewski.its.query.repository.IssueCommentRepository;
import com.karczewski.its.query.repository.IssueProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueProjectionQueryService implements IssueProjectionQueryClient {

    private final IssueProjectionRepository issueProjectionRepository;
    private final IssueCommentRepository issueCommentRepository;

    @Override
    public IssueProjection getIssueProjection(UUID uuid) {
        return issueProjectionRepository.findByUuid(uuid)
                .orElseThrow(() -> new IssueNotFoundException("Unable to find issue with uuid: " + uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssueProjection> getAssignedIssues(IssueFilters filters, int offset, int limit) {
        // TODO
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssueProjection> getReportedIssues(IssueFilters filters, int offset, int limit) {
        // TODO
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssueComment> getIssueComments(UUID uuid, int offset, int limit) {
        // TODO
        return null;
    }
}
