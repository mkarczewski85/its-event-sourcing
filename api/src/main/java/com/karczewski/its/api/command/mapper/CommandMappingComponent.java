package com.karczewski.its.api.command.mapper;

import com.karczewski.its.api.command.dto.request.*;
import com.karczewski.its.attachments.dto.AttachmentsUploadDto;
import com.karczewski.its.es.app.domain.command.*;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.security.authentication.AuthenticationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CommandMappingComponent {

    private final AuthenticationClient authenticationClient;

    public Command toCommand(ReportIssueRequestDto request) {
        return ReportIssueCommand.builder()
                .issueTitle(request.getTitle())
                .issueDescription(request.getDescription())
                .issueType(request.getType())
                .issueSeverity(request.getSeverity())
                .reportedBy(authenticationClient.getLoggedUserUuid())
                .build();
    }

    public Command toCommand(AssignIssueRequestDto request) {
        return ReassignIssueCommand.builder()
                .aggregateId(request.getUuid())
                .assignedBy(authenticationClient.getLoggedUserUuid())
                .assignedTo(request.getAssignTo())
                .build();
    }

    public Command toCommand(UpdateIssueStatusRequestDto request) {
        UUID loggedUser = authenticationClient.getLoggedUserUuid();
        return switch (request.getStatus()) {
            case IN_PROGRESS -> new AcceptIssueCommand(request.getUuid(), loggedUser);
            case REJECTED -> new RejectIssueCommand(request.getUuid(), loggedUser);
            case CANCELLED -> new CancelIssueCommand(request.getUuid(), loggedUser);
            case RESOLVED -> new ResolveIssueCommand(request.getUuid(), loggedUser);
            case null -> throw new RuntimeException();
        };
    }

    public Command toCommand(PublishCommentRequestDto request) {
        return CommentIssueCommand.builder()
                .aggregateId(request.getUuid())
                .comment(request.getContent())
                .authoredBy(authenticationClient.getLoggedUserUuid())
                .build();
    }

    public Collection<Command> toCommands(PatchIssueRequestDto request) {
        List<Command> commands = new ArrayList<>();
        addCommandIfNotNull(commands, request.getSeverity(),
                severity -> new UpdateIssueSeverityCommand(request.getUuid(), severity, authenticationClient.getLoggedUserUuid()));
        addCommandIfNotNull(commands, request.getType(),
                type -> new UpdateIssueTypeCommand(request.getUuid(), type, authenticationClient.getLoggedUserUuid()));
        return commands;
    }

    public AttachmentsUploadDto toDto(MultipartFile file, UUID issueId) throws IOException {
        return AttachmentsUploadDto.builder()
                .issueId(issueId)
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .data(file.getBytes())
                .build();
    }

    private <T> void addCommandIfNotNull(List<Command> commands,
                                         T value,
                                         Function<T,Command> factory) {
        if (value != null) {
            commands.add(factory.apply(value));
        }
    }

}
