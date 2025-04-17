package com.karczewski.its.es.core.service;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AggregateFactory {

    private final AggregateClassResolver aggregateClassResolver;

    @SuppressWarnings("unchecked")
    public <T extends Aggregate> T newInstance(String aggregateType, UUID aggregateId) {
        Class<? extends Aggregate> aggregateClass = aggregateClassResolver.getAggregateClass(aggregateType);
        try {
            Constructor<? extends Aggregate> constructor = aggregateClass.getDeclaredConstructor(UUID.class, int.class);
            return (T) constructor.newInstance(aggregateId, 0);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to instantiate aggregate: " + aggregateClass.getName(), e);
        }
    }
}
