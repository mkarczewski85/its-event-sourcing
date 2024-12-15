package com.karczewski.its.api.query.mapper;

import com.karczewski.its.api.query.dto.IssueCommentDto;
import com.karczewski.its.api.query.dto.IssueProjectionDto;
import com.karczewski.its.api.query.dto.IssueProjectionItemDto;
import com.karczewski.its.api.query.dto.UserDto;
import com.karczewski.its.query.entity.IssueComment;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.entity.User;
import com.karczewski.its.query.service.filters.IssueFilters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QueryMappingComponent {

    public IssueProjectionDto toDto(IssueProjection issueProjection) {
        return IssueProjectionDto.builder()
                .uuid(issueProjection.getId())
                .title(issueProjection.getTitle())
                .description(issueProjection.getDescription())
                .type(issueProjection.getType())
                .status(issueProjection.getStatus())
                .severity(issueProjection.getSeverity())
                .reportedBy(toDto(issueProjection.getReportedBy()))
                .assignedTo(toDto(issueProjection.getAssignedTo()))
                .reportedAt(issueProjection.getReportedAt())
                .updatedAt(issueProjection.getUpdatedAt())
                .build();
    }

    public IssueProjectionItemDto toItemDto(IssueProjection issueProjection) {
        return IssueProjectionItemDto.builder()
                .id(issueProjection.getId())
                .title(issueProjection.getTitle())
                .status(issueProjection.getStatus())
                .type(issueProjection.getType())
                .severity(issueProjection.getSeverity())
                .reportedAt(issueProjection.getReportedAt())
                .build();
    }

    public IssueCommentDto toDto(IssueComment issueComment) {
        return IssueCommentDto.builder()
                .content(issueComment.getContent())
                .authoredBy(toDto(issueComment.getAuthoredBy()))
                .publishedAt(issueComment.getPublishedAt())
                .build();
    }

    public IssueFilters toFilters(UUID uuid,
                                  String titlePhrase,
                                  String status,
                                  String severity,
                                  String type) {
        return IssueFilters.builder()
                .projectionId(uuid)
                .titlePhrase(titlePhrase)
                .status(status)
                .severity(severity)
                .type(type)
                .build();
    }

    private UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

}
