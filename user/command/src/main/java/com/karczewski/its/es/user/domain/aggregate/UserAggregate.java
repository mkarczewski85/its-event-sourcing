package com.karczewski.its.es.user.domain.aggregate;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import jakarta.annotation.Nonnull;
import lombok.NonNull;

import java.util.UUID;

public class UserAggregate extends Aggregate {
    protected UserAggregate(@NonNull UUID aggregateId, int version) {
        super(aggregateId, version);
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return "USER";
    }
}
