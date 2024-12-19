package com.karczewski.its.user.notification.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "email.messages.enabled",
        havingValue = "false",
        matchIfMissing = false
)
@Slf4j
public class NoEmailMessageSender implements MessageSender {
    @Override
    public void sendMessage(String[] to, String subject, String message) {
        log.info("Email messages disabled");
        log.info("Undelivered message \n\tto: \n\t\t{}, \n\tubject: \n\t\t{}, \n\tcontent: \n\t\t{}", to[0], subject, message);
    }
}
