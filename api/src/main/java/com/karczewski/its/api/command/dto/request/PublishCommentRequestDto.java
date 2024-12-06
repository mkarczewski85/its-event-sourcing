package com.karczewski.its.api.command.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class PublishCommentRequestDto {

    @NotBlank
    private UUID uuid;
    @NotBlank
    @Min(2)
    @Max(1024)
    private String content;

}
