package com.karczewski.its.api.command.dto.request;

import jakarta.validation.constraints.*;
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

    @NotNull
    private UUID uuid;
    @NotBlank
    @Size(max = 1048)
    private String content;

}
