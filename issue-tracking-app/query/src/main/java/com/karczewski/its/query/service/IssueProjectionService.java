package com.karczewski.its.query.service;

import com.karczewski.its.query.IssueProjectionClient;
import com.karczewski.its.query.dto.IssueCommentDto;
import com.karczewski.its.query.dto.IssueProjectionDto;
import com.karczewski.its.query.entity.IssueComment;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.repository.IssueProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueProjectionService implements IssueProjectionClient {

    private final IssueProjectionRepository issueProjectionRepository;

    @Override
    @Transactional
    public void updateIssueProjection(IssueProjectionDto dto) {
        IssueProjection issueProjection = issueProjectionRepository.findByUuid(dto.issueUuid())
                .orElse(IssueProjection.newProjection(dto.issueUuid()));
        issueProjection.setTitle(dto.title());
        issueProjection.setDescription(dto.description());
        issueProjection.setStatus(dto.status());
        issueProjection.setSeverity(dto.severity());
        issueProjection.setType(dto.type());
        issueProjection.setReportedBy(dto.reportedBy());
        issueProjection.setAssignedTo(dto.assignedTo());
    }

    @Override
    @Transactional
    public void addIssueComment(IssueCommentDto dto) {
        IssueComment issueComment = IssueComment.builder()
                .content(dto.content())
                .authoredBy(dto.authoredBy())
                .publishedAt(dto.publishedAt())
                .build();
        IssueProjection issueProjection = issueProjectionRepository.findByUuid(dto.issueUuid()).orElseThrow();
        issueProjection.addComment(issueComment);
    }
}
