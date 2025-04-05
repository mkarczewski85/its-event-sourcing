package com.karczewski.its.es.core.repository.postgresql;

import com.karczewski.its.es.core.domain.event.EventSubscriptionCheckpoint;
import com.karczewski.its.es.core.repository.EventSubscriptionRepository;
import com.karczewski.its.es.core.repository.postgresql.constants.ParameterNames;
import com.karczewski.its.es.core.repository.postgresql.constants.SqlQueries;
import com.karczewski.its.es.core.repository.postgresql.helpers.RowMappers;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@Transactional(propagation = Propagation.MANDATORY)
@Repository
@RequiredArgsConstructor
public class PostgresqlEventSubscriptionRepository implements EventSubscriptionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMappers rowMappers;

    public void createSubscriptionIfAbsent(String subscriptionName) {
        jdbcTemplate.update(
                SqlQueries.INSERT_EVENT_SUBSCRIPTION_QUERY,
                Map.of(ParameterNames.SUBSCRIPTION_NAME_PARAM, subscriptionName)
        );
    }

    public Optional<EventSubscriptionCheckpoint> findCheckpointAndLockSubscription(String subscriptionName) {
        return jdbcTemplate.query(
                SqlQueries.SELECT_EVENT_SUBSCRIPTION_QUERY,
                Map.of(ParameterNames.SUBSCRIPTION_NAME_PARAM, subscriptionName),
                rowMappers::mapEventSubscriptionCheckpoint
        ).stream().findFirst();
    }

    public void updateEventSubscription(String subscriptionName,
                                        BigInteger lastProcessedTransactionId,
                                        long lastProcessedEventId) {
        jdbcTemplate.update(
            SqlQueries.UPDATE_EVENT_SUBSCRIPTION_QUERY,
            Map.of(
                    ParameterNames.SUBSCRIPTION_NAME_PARAM, subscriptionName,
                    ParameterNames.LAST_PROCESSED_TRANSACTION_ID_PARAM, lastProcessedTransactionId.toString(),
                    ParameterNames.LAST_PROCESSED_EVENT_ID_PARAM, lastProcessedEventId
            )
        );
    }

}
