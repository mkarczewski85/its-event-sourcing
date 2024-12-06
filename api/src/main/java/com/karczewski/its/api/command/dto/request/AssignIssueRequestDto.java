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
public class AssignIssueRequestDto {

    @NotBlank
    private UUID uuid;
    @NotBlank
    private UUID assignedTo;

}
