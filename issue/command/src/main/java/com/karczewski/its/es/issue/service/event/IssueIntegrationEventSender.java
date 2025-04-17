package com.karczewski.its.es.issue.service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karczewski.its.es.issue.config.RabbitMQConfiguration;
import com.karczewski.its.es.issue.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.service.event.AsyncEventHandler;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IssueIntegrationEventSender implements AsyncEventHandler {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void handleEvent(EventWithId<Event> event) {
        log.debug("Sending notification event {}", event);
        String eventType = event.event().getEventType();
        String routingKey = "issues." + eventType;
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, routingKey, toJSON(event));
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return AggregateType.ISSUE.toString();
    }

    private String toJSON(EventWithId<Event> event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while converting event to JSON", e);
        }
    }
}
