package com.karczewski.its.user.component;

import com.karczewski.its.user.dto.UserPasswordChangeRequestDto;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.exception.UserAccountNotFoundException;
import com.karczewski.its.user.exception.UserAccountPermissionDeniedException;
import com.karczewski.its.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPasswordChangeComponent {

    private final UserCredentialsCreateComponent userCredentialsCreateComponent;
    private final UserPasswordValidateComponent passwordValidationService;
    private final UserAccountRepository userRepository;

    @Transactional
    public void updateUserCredentials(UUID userId, UserPasswordChangeRequestDto dto) {
        final UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new UserAccountNotFoundException("Unable to find user with UUID: " + userId));
        if (passwordValidationService.isPasswordValid(user.getUserCredentials(), dto.getOldPassword())) {
            userCredentialsCreateComponent.recreateUserCredentials(user.getUserCredentials(), dto.getNewPassword());
            userRepository.save(user);
        } else {
            throw new UserAccountPermissionDeniedException("Unauthorized access");
        }
    }

}
