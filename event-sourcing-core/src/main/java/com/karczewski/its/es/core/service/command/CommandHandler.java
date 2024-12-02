package com.karczewski.its.es.core.service.command;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import jakarta.annotation.Nonnull;

public interface CommandHandler<T extends Command> {

    void handle(Aggregate aggregate, Command command);

    @Nonnull
    Class<T> getCommandType();

}
