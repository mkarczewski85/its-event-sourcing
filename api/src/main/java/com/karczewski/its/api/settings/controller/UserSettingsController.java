package com.karczewski.its.api.settings.controller;

import com.karczewski.its.security.authentication.AuthenticationClient;
import com.karczewski.its.user.UserClient;
import com.karczewski.its.user.dto.PatchUserSettingsRequestDto;
import com.karczewski.its.user.dto.ResetPasswordByTokenRequestDto;
import com.karczewski.its.user.dto.SendPasswordResetTokenRequestDto;
import com.karczewski.its.user.dto.UserPasswordChangeRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@RequestMapping(UserSettingsController.BASE_PATH)
@RequiredArgsConstructor
public class UserSettingsController {
    static final String BASE_PATH = "${rest.prefix}";
    private static final String PASSWORD_RESET_PATH = "${rest.public.path}/reset-password";
    private static final String SEND_RESET_TOKEN_PATH = "${rest.public.path}/send-reset-token";
    private static final String PASSWORD_CHANGE_PATH = "${rest.secured.path}/password-change";
    private static final String PATCH_SETTINGS_PATH = "${rest.secured.path}/notifications-settings";

    private final UserClient userClient;
    private final AuthenticationClient authenticationClient;

    @PostMapping(PASSWORD_CHANGE_PATH)
    public void changeUserPassword(@RequestBody @Valid final UserPasswordChangeRequestDto dto) {
        UUID userUuid = authenticationClient.getLoggedUserUuid();
        userClient.changeUserPassword(userUuid, dto);
    }

    @PostMapping(SEND_RESET_TOKEN_PATH)
    public void sendUserPasswordResetToken(@RequestBody @Valid final SendPasswordResetTokenRequestDto dto) {
        userClient.sendPasswordResetToken(dto.getEmail());
    }

    @PostMapping(PASSWORD_RESET_PATH)
    public void resetUserPasswordByToken(@RequestBody @Valid final ResetPasswordByTokenRequestDto dto) {
        userClient.resetUserPasswordByToken(dto);
    }

    @PatchMapping(PATCH_SETTINGS_PATH)
    public void patchUserSettings(@RequestBody @Valid final PatchUserSettingsRequestDto dto) {

    }

}
