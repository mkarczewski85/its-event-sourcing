package com.karczewski.its.api.command.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIssueStatusRequestDto {

    @NotNull
    private UUID uuid;
    @NotNull
    private IssueUpdateStatus status;

    public enum IssueUpdateStatus {
        IN_PROGRESS, REJECTED, CANCELLED, RESOLVED
    }

}
