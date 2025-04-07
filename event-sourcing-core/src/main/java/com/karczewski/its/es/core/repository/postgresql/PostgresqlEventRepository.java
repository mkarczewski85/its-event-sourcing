package com.karczewski.its.es.core.repository.postgresql;

import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.repository.EventRepository;
import com.karczewski.its.es.core.repository.postgresql.constants.ParameterNames;
import com.karczewski.its.es.core.repository.postgresql.constants.SqlQueries;
import com.karczewski.its.es.core.repository.postgresql.helpers.JsonMapper;
import com.karczewski.its.es.core.repository.postgresql.helpers.RowMapper;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Types;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class PostgresqlEventRepository implements EventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JsonMapper jsonMapper;
    private final RowMapper rowMapper;

    @Override
    public <T extends Event> EventWithId<T> appendEvent(@NonNull Event event) {
        String json = jsonMapper.toJson(event);
        List<EventWithId<T>> result = jdbcTemplate.query(
                SqlQueries.INSERT_EVENT_QUERY,
                appendEventParams(event.getAggregateId(), event.getVersion(), event.getEventType(), json),
                rowMapper::mapEventWithId
        );
        return result.getFirst();
    }

    @Override
    public Collection<EventWithId<Event>> readEvents(@NonNull UUID aggregateId,
                                                     @Nullable Integer fromVersion,
                                                     @Nullable Integer toVersion) {
        return jdbcTemplate.query(
                SqlQueries.READ_EVENTS_QUERY,
                readEventsParameters(aggregateId, fromVersion, toVersion),
                rowMapper::mapEventWithId
        );
    }

    @Override
    public List<EventWithId<Event>> readEventsAfterCheckpoint(@NonNull String aggregateType,
                                                              @NonNull BigInteger lastProcessedTransactionId,
                                                              long lastProcessedEventId) {
        return jdbcTemplate.query(
                SqlQueries.READ_EVENTS_AFTER_CHECKPOINT_QUERY,
                readEventsAfterCheckpointParams(aggregateType, lastProcessedTransactionId, lastProcessedEventId),
                rowMapper::mapEventWithId
        );
    }

    private static Map<String, Object> appendEventParams(UUID aggregateId,
                                                                         Integer version,
                                                                         String eventType,
                                                                         String json) {
        return Map.of(
                ParameterNames.AGGREGATE_ID_PARAM, aggregateId,
                ParameterNames.VERSION_PARAM, version,
                ParameterNames.EVENT_TYPE_PARAM, eventType,
                ParameterNames.JSON_OBJ_PARAM, json
        );
    }

    private static Map<String, Object> readEventsAfterCheckpointParams(String aggregateType, BigInteger lastProcessedTransactionId, long lastProcessedEventId) {
        return Map.of(
                ParameterNames.AGGREGATE_TYPE_PARAM, aggregateType,
                ParameterNames.LAST_PROCESSED_TRANSACTION_ID_PARAM, lastProcessedTransactionId.toString(),
                ParameterNames.LAST_PROCESSED_EVENT_ID_PARAM, lastProcessedEventId
        );
    }

    private static MapSqlParameterSource readEventsParameters(UUID aggregateId, Integer fromVersion, Integer toVersion) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ParameterNames.AGGREGATE_ID_PARAM, aggregateId);
        parameters.addValue(ParameterNames.FROM_VERSION_PARAM, fromVersion, Types.INTEGER);
        parameters.addValue(ParameterNames.TO_VERSION_PARAM, toVersion, Types.INTEGER);
        return parameters;
    }
}
