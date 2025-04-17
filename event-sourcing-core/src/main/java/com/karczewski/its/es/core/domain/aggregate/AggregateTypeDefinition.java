package com.karczewski.its.es.core.domain.aggregate;

public interface AggregateTypeDefinition {

    boolean supports(String type);

    Class<? extends Aggregate> getAggregateClass();

}
