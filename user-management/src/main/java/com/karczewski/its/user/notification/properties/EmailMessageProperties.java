package com.karczewski.its.user.notification.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailMessageProperties {

    public final static String FROM_ACCOUNT = "m.karczewski1985@gmail.com";

    private String username;

}
