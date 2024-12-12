package com.karczewski.its.api.query.controller;

import com.karczewski.its.api.pagination.PageWrapper;
import com.karczewski.its.api.query.dto.IssueCommentDto;
import com.karczewski.its.api.query.dto.IssueProjectionDto;
import com.karczewski.its.api.query.dto.IssueProjectionItemDto;
import com.karczewski.its.api.query.mapper.QueryMappingComponent;
import com.karczewski.its.query.IssueProjectionQueryClient;
import com.karczewski.its.query.service.filters.IssueFilters;
import jakarta.annotation.Nullable;
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
            @Nullable @RequestParam(required = false, name = "id")  UUID id,
            @Nullable @RequestParam(required = false, name = "titlePhrase") String titlePhrase,
            @Nullable @RequestParam(required = false, name = "status") String status,
            @Nullable @RequestParam(required = false, name = "severity") String severity,
            @Nullable @RequestParam(required = false, name = "type") String type,
            @RequestParam(required = false, defaultValue = "0", name = "offset") @Min(0) int offset,
            @RequestParam(required = false, defaultValue = "20", name = "limit") @Min(20) int limit
    ) {
        IssueFilters filters = queryMappingComponent.toFilters(id, titlePhrase, status, severity, type);
        return PageWrapper.from(issueProjectionQueryClient.getReportedIssues(filters, offset, limit)
                .map(queryMappingComponent::toItemDto));
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public PageWrapper<IssueProjectionItemDto> getAssignedIssues(
            @Nullable @RequestParam(required = false, name = "id")  UUID id,
            @Nullable @RequestParam(required = false, name = "titlePhrase") String titlePhrase,
            @Nullable @RequestParam(required = false, name = "status") String status,
            @Nullable @RequestParam(required = false, name = "severity") String severity,
            @Nullable @RequestParam(required = false, name = "type") String type,
            @RequestParam(required = false, defaultValue = "0", name = "offset") @Min(0) int offset,
            @RequestParam(required = false, defaultValue = "20", name = "limit") @Min(20) int limit
    ) {
        IssueFilters filters = queryMappingComponent.toFilters(id, titlePhrase, status, severity, type);
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
