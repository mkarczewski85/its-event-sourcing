package com.karczewski.its.es.core.service;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.aggregate.AggregateTypeDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AggregateClassResolver {

    private final Set<AggregateTypeDefinition> aggregateDefinitions;

    public Class<? extends Aggregate> getAggregateClass(String aggregateType) {
        return aggregateDefinitions.stream()
                .filter(d -> d.supports(aggregateType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown aggregate type: " + aggregateType))
                .getAggregateClass();
    }

}
