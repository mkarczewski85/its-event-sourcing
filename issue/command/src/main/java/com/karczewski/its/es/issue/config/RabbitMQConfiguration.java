package com.karczewski.its.es.issue.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String EXCHANGE_NAME = "issues.events";

    public static final String NOTIFICATIONS_QUEUE = "notifications.send-alert.issues";
    public static final String ANALYTICS_QUEUE = "analytics.track-events.issues";

    @Bean
    public TopicExchange issuesExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(NOTIFICATIONS_QUEUE, true);
    }

    @Bean
    public Queue analyticsQueue() {
        return new Queue(ANALYTICS_QUEUE, true);
    }

    @Bean
    public Binding notificationsBinding(@Qualifier("notificationsQueue") Queue notificationsQueue, TopicExchange issuesExchange) {
        return BindingBuilder.bind(notificationsQueue).to(issuesExchange).with("issues.#");
    }

    @Bean
    public Binding analyticsBinding(@Qualifier("analyticsQueue") Queue analyticsQueue, TopicExchange issuesExchange) {
        return BindingBuilder.bind(analyticsQueue).to(issuesExchange).with("issues.#");
    }

}
