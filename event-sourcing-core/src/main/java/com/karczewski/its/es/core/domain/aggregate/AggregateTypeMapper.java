package com.karczewski.its.es.core.domain.aggregate;

public interface AggregateTypeMapper {

    Class<? extends Aggregate> getClassByAggregateType(String aggregateType);

}
