package com.karczewski.its.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserSettingsRequestDto {

    @NotNull
    private Boolean issueNotifications;
    @NotNull
    private Boolean commentsNotifications;

}
