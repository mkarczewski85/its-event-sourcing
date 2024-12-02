package com.karczewski.its.es.core.service;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.service.command.CommandHandler;
import com.karczewski.its.es.core.service.command.DefaultCommandHandler;
import com.karczewski.its.es.core.service.event.SyncEventHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class CommandProcessor {

    private final AggregateStore aggregateStore;
    private final List<CommandHandler<? extends Command>> commandHandlers;
    private final DefaultCommandHandler defaultCommandHandler;
    private final List<SyncEventHandler> aggregateChangesHandlers;

    public Aggregate process(@NonNull Command command) {
        log.debug("Processing command {}", command);

        String aggregateType = command.getAggregateType();
        UUID aggregateId = command.getAggregateId();

        Aggregate aggregate = aggregateStore.readAggregate(aggregateType, aggregateId);

        commandHandlers.stream()
                .filter(commandHandler -> commandHandler.getCommandType() == command.getClass())
                .findFirst()
                .ifPresentOrElse(commandHandler -> {
                    log.debug("Handling command {} with {}",
                            command.getClass().getSimpleName(), commandHandler.getClass().getSimpleName());
                    commandHandler.handle(aggregate, command);
                }, () -> {
                    log.debug("No specialized handler found, handling command {} with {}",
                            command.getClass().getSimpleName(), defaultCommandHandler.getClass().getSimpleName());
                    defaultCommandHandler.handle(aggregate, command);
                });

        List<EventWithId<Event>> newEvents = aggregateStore.saveAggregate(aggregate);

        aggregateChangesHandlers.stream()
                .filter(handler -> handler.getAggregateType().equals(aggregateType))
                .forEach(handler -> handler.handleEvents(newEvents, aggregate));

        return aggregate;
    }
}

