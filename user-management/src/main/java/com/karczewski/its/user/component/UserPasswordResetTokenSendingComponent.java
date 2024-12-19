package com.karczewski.its.user.component;

import com.karczewski.its.user.entity.PasswordResetToken;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordResetTokenSendingComponent {

    public void sendUserPasswordResetToken(PasswordResetToken resetToken, String email) {
        // TODO
    }

}
