package com.karczewski.its.es.core.repository.postgresql;

import com.karczewski.its.es.core.domain.event.EventSubscriptionCheckpoint;
import com.karczewski.its.es.core.repository.EventSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Transactional(propagation = Propagation.MANDATORY)
@Repository
@RequiredArgsConstructor
public class PostgresqlEventSubscriptionRepository implements EventSubscriptionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void createSubscriptionIfAbsent(String subscriptionName) {
        jdbcTemplate.update(
                SqlQueries.INSERT_EVENT_SUBSCRIPTION_QUERY,
                Map.of("subscriptionName", subscriptionName)
        );
    }

    public Optional<EventSubscriptionCheckpoint> findCheckpointAndLockSubscription(String subscriptionName) {
        return jdbcTemplate.query(
                SqlQueries.SELECT_EVENT_SUBSCRIPTION_QUERY,
                Map.of("subscriptionName", subscriptionName),
                this::toEventSubscriptionCheckpoint
        ).stream().findFirst();
    }

    public boolean updateEventSubscription(String subscriptionName,
                                           BigInteger lastProcessedTransactionId,
                                           long lastProcessedEventId) {
        int updatedRows = jdbcTemplate.update(
                SqlQueries.UPDATE_EVENT_SUBSCRIPTION_QUERY,
                Map.of(
                        "subscriptionName", subscriptionName,
                        "lastProcessedTransactionId", lastProcessedTransactionId.toString(),
                        "lastProcessedEventId", lastProcessedEventId
                )
        );
        return updatedRows > 0;
    }

    private EventSubscriptionCheckpoint toEventSubscriptionCheckpoint(ResultSet rs, int rowNum) throws SQLException {
        String lastProcessedTransactionId = rs.getString("LAST_TRANSACTION_ID");
        long lastProcessedEventId = rs.getLong("LAST_EVENT_ID");
        return new EventSubscriptionCheckpoint(new BigInteger(lastProcessedTransactionId), lastProcessedEventId);
    }

}
