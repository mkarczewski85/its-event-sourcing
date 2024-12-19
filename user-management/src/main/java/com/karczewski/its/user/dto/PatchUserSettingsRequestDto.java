package com.karczewski.its.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserSettingsRequestDto {

    private Boolean issueNotifications;
    private Boolean commentsNotifications;

}
