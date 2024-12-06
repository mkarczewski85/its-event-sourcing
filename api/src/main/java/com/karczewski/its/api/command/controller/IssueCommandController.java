package com.karczewski.its.api.command.controller;

import com.karczewski.its.api.command.dto.request.*;
import com.karczewski.its.api.command.dto.response.IssueCommandResponseDto;
import com.karczewski.its.api.command.mapper.CommandMappingComponent;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.service.CommandProcessor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @PreAuthorize("hasRole('ROLE_REPORTER')")
    public IssueCommandResponseDto reportIssue(@RequestBody @Valid @NotNull final ReportIssueRequestDto request) {
        Command command = commandMappingComponent.toCommand(request);
        Aggregate aggregate = commandProcessor.process(command);
        return new IssueCommandResponseDto(aggregate.getAggregateId());
    }

    @PutMapping("/{uuid}/status")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public IssueCommandResponseDto updateIssueStatus(@PathVariable final UUID uuid,
                                                     @RequestBody @Valid @NotNull final UpdateIssueStatusRequestDto request) {
        request.setUuid(uuid);
        Command command = commandMappingComponent.toCommand(request);
        Aggregate aggregate = commandProcessor.process(command);
        return new IssueCommandResponseDto(aggregate.getAggregateId());
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public IssueCommandResponseDto patchIssue(@PathVariable final UUID uuid,
                                              @RequestBody @Valid @NotNull final PatchIssueRequestDto request) {
        request.setUuid(uuid);
        Collection<Command> commands = commandMappingComponent.toCommands(request);
        if (!commands.isEmpty()) {
            commandProcessor.process(commands);
        }
        return IssueCommandResponseDto.builder().uuid(uuid).build();
    }

    @PostMapping("/{uuid/assign}")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public IssueCommandResponseDto assignIssue(@PathVariable final UUID uuid,
                            @RequestBody @Valid @NotNull final AssignIssueRequestDto request) {
        request.setUuid(uuid);
        Command commands = commandMappingComponent.toCommand(request);
        Aggregate aggregate = commandProcessor.process(commands);
        return new IssueCommandResponseDto(aggregate.getAggregateId());
    }

    @PostMapping("/{uuid}/comments")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN') or hasRole('ROLE_REPORTER')")
    public IssueCommandResponseDto publishIssueComment(@PathVariable final UUID uuid,
                                                       @RequestBody @Valid @NotNull final PublishCommentRequestDto request) {
        request.setUuid(uuid);
        Command commands = commandMappingComponent.toCommand(request);
        Aggregate aggregate = commandProcessor.process(commands);
        return new IssueCommandResponseDto(aggregate.getAggregateId());
    }

}
