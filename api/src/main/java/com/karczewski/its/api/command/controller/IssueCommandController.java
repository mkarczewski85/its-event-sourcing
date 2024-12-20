package com.karczewski.its.api.command.controller;

import com.karczewski.its.api.command.dto.request.*;
import com.karczewski.its.api.command.dto.response.IssueCommandResponseDto;
import com.karczewski.its.api.command.mapper.CommandMappingComponent;
import com.karczewski.its.attachments.AttachmentsClient;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.CommandProcessor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@RestController
@Validated
@RequestMapping(IssueCommandController.BASE_PATH)
@RequiredArgsConstructor
public class IssueCommandController {
    static final String BASE_PATH = "${rest.prefix}${rest.secured.path}/issues";

    private final CommandProcessor commandProcessor;
    private final CommandMappingComponent commandMappingComponent;
    private final AttachmentsClient attachmentsClient;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_REPORTER')")
    public IssueCommandResponseDto reportIssue(
            @RequestPart(value = "json") @Valid @NotNull ReportIssueRequestDto request,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment) throws IOException {
        Command command = commandMappingComponent.toCommand(request);
        Aggregate aggregate = commandProcessor.process(command);
        if (attachment != null) {
            attachmentsClient.saveAttachments(commandMappingComponent.toDto(attachment, aggregate.getAggregateId()));
        }
        return new IssueCommandResponseDto(aggregate.getAggregateId());
    }

    @PutMapping("/{uuid}/status")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public IssueCommandResponseDto updateIssueStatus(@PathVariable("uuid") final UUID uuid,
                                                     @RequestBody @Valid @NotNull final UpdateIssueStatusRequestDto request) {
        request.setUuid(uuid);
        Command command = commandMappingComponent.toCommand(request);
        Aggregate aggregate = commandProcessor.process(command);
        return new IssueCommandResponseDto(aggregate.getAggregateId());
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public IssueCommandResponseDto patchIssue(@PathVariable("uuid") final UUID uuid,
                                              @RequestBody @Valid @NotNull final PatchIssueRequestDto request) {
        request.setUuid(uuid);
        Collection<Command> commands = commandMappingComponent.toCommands(request);
        if (!commands.isEmpty()) {
            commandProcessor.process(commands);
        }
        return IssueCommandResponseDto.builder().uuid(uuid).build();
    }

    @PostMapping("/{uuid}/assign")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public IssueCommandResponseDto assignIssue(@PathVariable("uuid") final UUID uuid,
                                               @RequestBody @Valid @NotNull final AssignIssueRequestDto request) {
        request.setUuid(uuid);
        Command commands = commandMappingComponent.toCommand(request);
        Aggregate aggregate = commandProcessor.process(commands);
        return new IssueCommandResponseDto(aggregate.getAggregateId());
    }

    @PostMapping("/{uuid}/comments")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN') or hasRole('ROLE_REPORTER')")
    public IssueCommandResponseDto publishIssueComment(@PathVariable("uuid") final UUID uuid,
                                                       @RequestBody @Valid @NotNull final PublishCommentRequestDto request) {
        request.setUuid(uuid);
        Command commands = commandMappingComponent.toCommand(request);
        Aggregate aggregate = commandProcessor.process(commands);
        return new IssueCommandResponseDto(aggregate.getAggregateId());
    }

}
