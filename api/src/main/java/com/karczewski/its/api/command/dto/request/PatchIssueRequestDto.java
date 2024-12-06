package com.karczewski.its.api.command.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchIssueRequestDto {

    private UUID uuid;
    private String severity;
    private String type;

}
