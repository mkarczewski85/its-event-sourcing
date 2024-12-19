package com.karczewski.its.user.component;

import com.karczewski.its.user.dto.PatchUserSettingsRequestDto;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserAccountSettings;
import com.karczewski.its.user.exception.UserAccountNotFoundException;
import com.karczewski.its.user.repository.UserAccountRepository;
import com.karczewski.its.user.repository.UserAccountSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserSettingsPatchComponent {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountSettingsRepository userAccountSettingsRepository;

    @Transactional
    public void patchUserSettings(UUID userId, PatchUserSettingsRequestDto dto) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UserAccountNotFoundException("Unable to find user account with id: " + userId));
        UserAccountSettings settings = userAccount.getUserAccountSettings();
        if (dto.getIssueNotifications() != null) {
            settings.setEnableIssueNotifications(dto.getIssueNotifications());
        }
        if (dto.getCommentsNotifications() != null) {
            settings.setEnableCommentNotifications(dto.getCommentsNotifications());
        }
        userAccountSettingsRepository.save(settings);
    }

}
