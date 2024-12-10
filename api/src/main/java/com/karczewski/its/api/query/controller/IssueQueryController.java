package com.karczewski.its.api.query.controller;

import com.karczewski.its.api.pagination.PageWrapper;
import com.karczewski.its.api.query.dto.IssueCommentDto;
import com.karczewski.its.api.query.dto.IssueProjectionDto;
import com.karczewski.its.api.query.dto.IssueProjectionItemDto;
import com.karczewski.its.api.query.mapper.QueryMappingComponent;
import com.karczewski.its.query.IssueProjectionQueryClient;
import com.karczewski.its.query.service.filters.IssueFilters;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@RequestMapping(IssueQueryController.BASE_PATH)
@RequiredArgsConstructor
public class IssueQueryController {

    static final String BASE_PATH = "${rest.prefix}${rest.secured.path}/issues";

    private final IssueProjectionQueryClient issueProjectionQueryClient;
    private final QueryMappingComponent queryMappingComponent;

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('ROLE_REPORTER') or hasRole('ROLE_TECHNICIAN')")
    public IssueProjectionDto getIssueProjection(@PathVariable("uuid") final UUID uuid) {
        return queryMappingComponent.toDto(issueProjectionQueryClient.getIssueProjection(uuid));
    }

    @GetMapping("/reported")
    @PreAuthorize("hasRole('ROLE_REPORTER')")
    public PageWrapper<IssueProjectionItemDto> getReportedIssues(
            @RequestParam(required = false) final UUID uuid,
            @RequestParam(required = false) final String titlePhrase,
            @RequestParam(required = false) final String status,
            @RequestParam(required = false) final String severity,
            @RequestParam(required = false) final String type,
            @RequestParam(defaultValue = "0") @Min(0) final int offset,
            @RequestParam(defaultValue = "20") @Min(20) final int limit
    ) {
        IssueFilters filters = queryMappingComponent.toFilters(uuid, titlePhrase, status, severity, type);
        return PageWrapper.from(issueProjectionQueryClient.getReportedIssues(filters, offset, limit)
                .map(queryMappingComponent::toItemDto));
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public PageWrapper<IssueProjectionItemDto> getAssignedIssues(
            @RequestParam(required = false) final UUID uuid,
            @RequestParam(required = false) final String titlePhrase,
            @RequestParam(required = false) final String status,
            @RequestParam(required = false) final String severity,
            @RequestParam(required = false) final String type,
            @RequestParam(defaultValue = "0") @Min(0) final int offset,
            @RequestParam(defaultValue = "20") @Min(20) final int limit
    ) {
        IssueFilters filters = queryMappingComponent.toFilters(uuid, titlePhrase, status, severity, type);
        return PageWrapper.from(issueProjectionQueryClient.getAssignedIssues(filters, offset, limit)
                .map(queryMappingComponent::toItemDto));
    }

    @GetMapping("/{uuid}/comments")
    @PreAuthorize("hasRole('ROLE_REPORTER') or hasRole('ROLE_TECHNICIAN')")
    public PageWrapper<IssueCommentDto> getIssueComments(
            @PathVariable("uuid") final UUID uuid,
            @RequestParam(defaultValue = "0") @Min(0) final int offset,
            @RequestParam(defaultValue = "20") @Min(20) final int limit
    ) {
        return PageWrapper.from(issueProjectionQueryClient.getIssueComments(uuid, offset, limit)
                .map(queryMappingComponent::toDto));
    }

}
