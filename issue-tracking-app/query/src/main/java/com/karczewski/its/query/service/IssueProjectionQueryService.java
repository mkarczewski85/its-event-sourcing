package com.karczewski.its.query.service;

import com.karczewski.its.query.IssueProjectionQueryClient;
import com.karczewski.its.query.entity.IssueComment;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.entity.UserIssueCount;
import com.karczewski.its.query.exception.IssueNotFoundException;
import com.karczewski.its.query.repository.IssueCommentRepository;
import com.karczewski.its.query.repository.IssueProjectionRepository;
import com.karczewski.its.query.repository.UserRepository;
import com.karczewski.its.query.service.filters.IssueFilters;
import com.karczewski.its.query.service.specification.IssueProjectionSpecification;
import com.karczewski.its.query.service.validation.UserPermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueProjectionQueryService implements IssueProjectionQueryClient {

    private final IssueProjectionSpecification projectionSpecification;
    private final IssueProjectionRepository issueProjectionRepository;
    private final IssueCommentRepository issueCommentRepository;
    private final UserRepository userRepository;
    private final UserPermissionValidator permissionValidator;

    @Override
    public IssueProjection getIssueProjection(UUID uuid) {
        IssueProjection issueProjection = issueProjectionRepository.findById(uuid)
                .orElseThrow(() -> new IssueNotFoundException("Unable to find issue with uuid: " + uuid));
        permissionValidator.validateUserPermission(issueProjection);
        return issueProjection;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssueProjection> getAssignedIssues(IssueFilters filters, int offset, int limit) {
        Specification<IssueProjection> specification = projectionSpecification.getAssignedIssueSpecification(filters);
        int pageNo = (limit + offset) / limit;
        PageRequest pageRequest = PageRequest.of(--pageNo, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return issueProjectionRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssueProjection> getReportedIssues(IssueFilters filters, int offset, int limit) {
        Specification<IssueProjection> specification = projectionSpecification.getReportedIssueSpecification(filters);
        int pageNo = (limit + offset) / limit;
        PageRequest pageRequest = PageRequest.of(--pageNo, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return issueProjectionRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IssueComment> getIssueComments(UUID uuid, int offset, int limit) {
        IssueProjection issueProjection = issueProjectionRepository.findById(uuid)
                .orElseThrow(() -> new IssueNotFoundException("Unable to find issue with uuid: " + uuid));
        permissionValidator.validateUserPermission(issueProjection);
        int pageNo = (limit + offset) / limit;
        PageRequest pageRequest = PageRequest.of(--pageNo, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return issueCommentRepository.findAllByIssue(issueProjection, pageRequest);
    }

    @Override
    public Collection<IssueProjection> getAllUnassignedIssues() {
        return issueProjectionRepository.findAll(projectionSpecification.getUnassignedIssuesSpecification());
    }

    @Override
    public List<UserIssueCount> getUserAssignmentsCount() {
        return userRepository.getUserAssignedIssuesCount();
    }
}
