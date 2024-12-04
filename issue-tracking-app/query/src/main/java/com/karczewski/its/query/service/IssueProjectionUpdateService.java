package com.karczewski.its.query.service;

import com.karczewski.its.query.IssueProjectionUpdateClient;
import com.karczewski.its.query.entity.IssueComment;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.model.IssueCommentModel;
import com.karczewski.its.query.model.IssueProjectionUpdateModel;
import com.karczewski.its.query.repository.IssueProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueProjectionUpdateService implements IssueProjectionUpdateClient {

    private final IssueProjectionRepository issueProjectionRepository;

    @Override
    @Transactional
    public void updateIssueProjection(IssueProjectionUpdateModel model) {
        IssueProjection issueProjection = issueProjectionRepository.findByUuid(model.uuid())
                .orElse(IssueProjection.newProjection(model.uuid()));
        issueProjection.setTitle(model.title());
        issueProjection.setDescription(model.description());
        issueProjection.setStatus(model.status());
        issueProjection.setSeverity(model.severity());
        issueProjection.setType(model.type());
        issueProjection.setReportedBy(model.reportedBy());
        issueProjection.setAssignedTo(model.assignedTo());
    }

    @Override
    @Transactional
    public void addIssueComment(IssueCommentModel model) {
        IssueComment issueComment = IssueComment.builder()
                .content(model.content())
                .authoredBy(model.authoredBy())
                .publishedAt(model.publishedAt())
                .build();
        IssueProjection issueProjection = issueProjectionRepository.findByUuid(model.issueUuid()).orElseThrow();
        issueProjection.addComment(issueComment);
    }
}
