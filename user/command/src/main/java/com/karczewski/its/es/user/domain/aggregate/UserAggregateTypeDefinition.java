package com.karczewski.its.es.user.domain.aggregate;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.aggregate.AggregateTypeDefinition;

public class UserAggregateTypeDefinition implements AggregateTypeDefinition {

    private static final String USER_TYPE = "USER";

    @Override
    public boolean supports(String type) {
        return USER_TYPE.equals(type);
    }

    @Override
    public Class<? extends Aggregate> getAggregateClass() {
        return UserAggregate.class;
    }
}
