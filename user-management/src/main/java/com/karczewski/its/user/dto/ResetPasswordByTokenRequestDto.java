package com.karczewski.its.user.dto;

import com.karczewski.its.user.validation.UserPasswordConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordByTokenRequestDto {

    @NotBlank
    private String resetToken;
    @UserPasswordConstraint
    private String newPassword;

}
