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
public class AssignIssueRequestDto {

    @NotNull
    private UUID uuid;
    @NotNull
    private UUID assignTo;

}
