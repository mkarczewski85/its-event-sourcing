package com.karczewski.its.user.notification;

import com.karczewski.its.user.notification.component.EmailMessageCreatingComponent;
import com.karczewski.its.user.notification.component.MessageSender;
import com.karczewski.its.user.notification.model.EmailMessageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationService {

    private final EmailMessageCreatingComponent messageComponent;
    private final MessageSender sender;

    public void sendWelcomeEmail(final String email, final String password) {
        final EmailMessageModel emailContent = messageComponent
                .getWelcomeEmail(email, password);
        sender.sendMessage(new String[]{email}, emailContent.getSubject(),
                emailContent.getMessage());
    }

    public void sendPasswordResetEmail(final String email, final String token) {
        final EmailMessageModel emailContent = messageComponent
                .getPasswordResetEmail(token);
        sender.sendMessage(new String[]{email}, emailContent.getSubject(),
                emailContent.getMessage());
    }

}
