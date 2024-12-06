package com.karczewski.its.es.core.service.command;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.command.Command;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultCommandInterceptor implements CommandInterceptor<Command> {
    @Override
    public void intercept(Aggregate aggregate, Command command) {
        // TODO
    }

    @Nonnull
    @Override
    public Class<Command> getCommandType() {
        return Command.class;
    }
}
