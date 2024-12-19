package com.karczewski.its.user.notification.component;

import com.karczewski.its.user.notification.properties.EmailMessageProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailMessageSender {

    private final EmailMessageProperties emailProperties;
    private final JavaMailSender javaMailSender;

    public void sendEmail(String[] to, String subject, String message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setFrom(emailProperties.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
        javaMailSender.send(mimeMessage);
    }

}
