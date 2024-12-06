package com.karczewski.its.api.command.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private UUID uuid;
    @NotBlank
    private IssueUpdateStatus status;

    public enum IssueUpdateStatus {
        IN_PROGRESS, REJECTED, CANCELLED, RESOLVED
    }

}
