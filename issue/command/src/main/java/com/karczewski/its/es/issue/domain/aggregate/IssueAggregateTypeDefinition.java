package com.karczewski.its.es.issue.domain.aggregate;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.aggregate.AggregateTypeDefinition;
import org.springframework.stereotype.Component;

@Component
public class IssueAggregateTypeDefinition implements AggregateTypeDefinition {

    @Override
    public boolean supports(String type) {
        return IssueAggregate.AGGREGATE_TYPE_NAME.equals(type);
    }

    @Override
    public Class<? extends Aggregate> getAggregateClass() {
        return IssueAggregate.class;
    }
}
