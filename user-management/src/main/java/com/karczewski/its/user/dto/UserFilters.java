package com.karczewski.its.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserFilters {

    private String uuid;
    private String namePhrase;
    private String emailPhrase;
    private String userRole;
    private Boolean isActive;

}
