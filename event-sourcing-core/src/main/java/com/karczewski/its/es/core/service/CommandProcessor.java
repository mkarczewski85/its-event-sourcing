package com.karczewski.its.es.core.service;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.service.command.CommandHandler;
import com.karczewski.its.es.core.service.command.CommandInterceptor;
import com.karczewski.its.es.core.service.command.DefaultCommandHandler;
import com.karczewski.its.es.core.service.command.DefaultCommandInterceptor;
import com.karczewski.its.es.core.service.event.SyncEventHandler;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class CommandProcessor {

    private final AggregateStore aggregateStore;
    private final Set<CommandHandler<? extends Command>> commandHandlers;
    private final Set<CommandInterceptor<? extends Command>> commandInterceptors;
    private final DefaultCommandHandler defaultCommandHandler;
    private final DefaultCommandInterceptor defaultCommandInterceptor;
    private final Set<SyncEventHandler> aggregateChangesHandlers;

    public void process(@NotEmpty Collection<Command> commands) {
        commands.forEach(this::process);
    }

    public Aggregate process(@NonNull Command command) {
        log.debug("Processing command {}", command);
        Aggregate aggregate = loadAggregate(command);
        interceptCommand(command, aggregate);
        handleCommand(command, aggregate);
        Collection<EventWithId<Event>> newEvents = saveAggregate(aggregate);
        notifyEventHandlers(command, aggregate, newEvents);
        return aggregate;
    }

    private Aggregate loadAggregate(Command command) {
        return aggregateStore.readAggregate(command.getAggregateType(), command.getAggregateId());
    }

    private void interceptCommand(Command command, Aggregate aggregate) {
        findInterceptorForCommand(command)
                .ifPresent(
                        interceptor -> {
                            log.debug("Intercepting command {} with {}",
                                    command.getClass().getSimpleName(),
                                    interceptor.getClass().getSimpleName());
                            interceptor.intercept(aggregate, command);
                        });
    }

    private void handleCommand(Command command, Aggregate aggregate) {
        findHandlerForCommand(command)
                .ifPresent(
                        handler -> {
                            log.debug("Handling command {} with {}",
                                    command.getClass().getSimpleName(),
                                    handler.getClass().getSimpleName());
                            handler.handle(aggregate, command);
                        });
    }

    private Collection<EventWithId<Event>> saveAggregate(Aggregate aggregate) {
        return aggregateStore.saveAggregate(aggregate);
    }

    private void notifyEventHandlers(Command command, Aggregate aggregate, Collection<EventWithId<Event>> newEvents) {
        aggregateChangesHandlers.stream()
                .filter(handler -> handler.getAggregateType().equals(command.getAggregateType()))
                .forEach(handler -> handler.handleEvents(newEvents, aggregate));
    }

    private Optional<CommandInterceptor<? extends Command>> findInterceptorForCommand(Command command) {
        return commandInterceptors.stream()
                .filter(interceptor -> interceptor.getCommandType() == command.getClass())
                .findFirst()
                .or(() -> Optional.of(defaultCommandInterceptor));
    }

    private Optional<CommandHandler<? extends Command>> findHandlerForCommand(Command command) {
        return commandHandlers.stream()
                .filter(handler -> handler.getCommandType() == command.getClass())
                .findFirst()
                .or(() -> Optional.of(defaultCommandHandler));
    }
}
