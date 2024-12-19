package com.karczewski.its.user.component;

import com.karczewski.its.user.dto.ResetPasswordByTokenRequestDto;
import com.karczewski.its.user.entity.PasswordResetToken;
import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserCredentials;
import com.karczewski.its.user.exception.InvalidPasswordResetTokenException;
import com.karczewski.its.user.repository.PasswordResetTokenRepository;
import com.karczewski.its.user.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAccountPasswordResetComponent {

    private final UserCredentialsRepository credentialsRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserCredentialsCreateComponent credentialsCreateComponent;

    @Transactional
    public PasswordResetToken generatePasswordResetToken(UserAccount userAccount) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .userCredentials(userAccount.getUserCredentials())
                .build();
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Transactional
    public void setNewPasswordByResetToken(ResetPasswordByTokenRequestDto dto) {
        Optional<PasswordResetToken> resetTokenOpt = passwordResetTokenRepository.findByToken(dto.getResetToken());
        if (resetTokenOpt.map(PasswordResetToken::isValid).orElse(false)) {
            PasswordResetToken resetToken = resetTokenOpt.get();
            UserCredentials userCredentials = resetToken.getUserCredentials();
            credentialsCreateComponent.recreateUserCredentials(userCredentials, dto.getNewPassword());
            credentialsRepository.save(userCredentials);
            passwordResetTokenRepository.delete(resetToken);
        } else {
            throw new InvalidPasswordResetTokenException("Bad token");
        }
    }

}
