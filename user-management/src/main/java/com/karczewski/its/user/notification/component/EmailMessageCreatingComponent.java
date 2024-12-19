package com.karczewski.its.user.notification.component;

import com.karczewski.its.user.notification.model.EmailMessageModel;
import com.karczewski.its.user.notification.model.EmailTemplate;
import com.karczewski.its.user.notification.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMessageCreatingComponent {

    private final ApplicationProperties applicationProperties;

    public EmailMessageModel getWelcomeEmail(final String login, final String rawPassword) {
        EmailTemplate content = EmailTemplate.WELCOME_EMAIL;
        String baseUrl = applicationProperties.getBaseUrl();
        return EmailMessageModel.builder()
                .message(String.format(content.getMessage(), login, rawPassword, baseUrl, baseUrl))
                .subject(content.getSubject())
                .build();
    }

    public EmailMessageModel getPasswordResetEmail(final String token) {
        final String url = applicationProperties.getBaseUrl() + "/reset-password/" + token;
        EmailTemplate content = EmailTemplate.PASSWORD_RESET_EMAIL;
        return EmailMessageModel.builder()
                .message(String.format(content.getMessage(), url, url))
                .subject(content.getSubject())
                .build();
    }

}
