package com.karczewski.its.es.app.domain.aggregate;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AggregateType {

    ISSUE(IssueAggregate.class);

    private final Class<? extends Aggregate> aggregateClass;

    public static AggregateType getByAggregateClass(Class<? extends Aggregate> clazz) {
        for (AggregateType aggregateType : AggregateType.values()) {
            if (aggregateType.getAggregateClass().equals(clazz)) {
                return aggregateType;
            }
        }
        throw new IllegalArgumentException("No AggregateType found for class: " + clazz.getName());
    }

}
