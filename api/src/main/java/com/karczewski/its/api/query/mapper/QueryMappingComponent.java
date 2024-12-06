package com.karczewski.its.api.query.mapper;

import com.karczewski.its.api.query.dto.response.IssueProjectionDto;
import com.karczewski.its.api.query.dto.response.IssueProjectionItemDto;
import com.karczewski.its.api.query.dto.response.UserDto;
import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.service.filters.IssueFilters;
import com.karczewski.its.security.authentication.AuthenticationClient;
import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QueryMappingComponent {

    private final UserClient userClient;
    private final AuthenticationClient authenticationClient;

    public IssueProjectionDto toDto(IssueProjection issueProjection) {
        return IssueProjectionDto.builder()
                .uuid(issueProjection.getUuid())
                .title(issueProjection.getTitle())
                .description(issueProjection.getDescription())
                .type(issueProjection.getType())
                .status(issueProjection.getStatus())
                .severity(issueProjection.getSeverity())
                .reportedBy(toDto(issueProjection.getReportedBy()))
                .assignedTo(toDto(issueProjection.getAssignedTo()))
                .reportedAt(issueProjection.getReportedAt())
                .build();
    }

    public IssueProjectionItemDto toItemDto(IssueProjection issueProjection) {
        return IssueProjectionItemDto.builder()
                .uuid(issueProjection.getUuid())
                .title(issueProjection.getTitle())
                .status(issueProjection.getStatus())
                .type(issueProjection.getType())
                .severity(issueProjection.getSeverity())
                .reportedAt(issueProjection.getReportedAt())
                .build();
    }

    public IssueFilters toFilters(UUID uuid,
                                  String titlePhrase,
                                  String status,
                                  String severity,
                                  String type) {
        return IssueFilters.builder()
                .projectionUuid(uuid)
                .titlePhrase(titlePhrase)
                .status(status)
                .severity(severity)
                .type(type)
                .userUuid(authenticationClient.getLoggedUserUuid())
                .build();
    }

    private UserDto toDto(UUID uuid) {
        if (uuid == null) return null;
        UserAccount userAccount = userClient.getByUUID(uuid);
        return UserDto.builder()
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .email(userAccount.getEmail())
                .department(userAccount.getDepartment().getName())
                .build();
    }

}
