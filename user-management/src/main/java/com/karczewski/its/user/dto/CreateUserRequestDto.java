package com.karczewski.its.user.dto;

import com.karczewski.its.user.entity.UserRole;
import com.karczewski.its.user.validation.ValueOfEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private long departmentId;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @ValueOfEnum(enumClass = UserRole.class)
    private String role;

}
