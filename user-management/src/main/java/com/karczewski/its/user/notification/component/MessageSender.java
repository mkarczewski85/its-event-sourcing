package com.karczewski.its.user.notification.component;

public interface MessageSender {

    void sendMessage(String[] to, String subject, String message);

}
