package com.karczewski.its.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserFilters {

    private UUID uuid;
    private UUID excludedUuid;
    private String namePhrase;
    private String emailPhrase;
    private String userRole;
    private Boolean isActive;

}
