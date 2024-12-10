package com.karczewski.its.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private UUID departmentId;
    private Boolean isActive;

}
