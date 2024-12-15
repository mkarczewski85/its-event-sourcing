package com.karczewski.its.api.command.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportIssueRequestDto {

    @NotBlank
    @Size(max = 150)
    private String title;
    @NotBlank
    @Size(max = 2500)
    private String description;
    @NotBlank
    private String severity;
    @NotBlank
    private String type;

}
