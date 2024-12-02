package com.karczewski.its.es.core.repository.postgresql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventTypeMapper;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.repository.EventRepository;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.ResultSet;
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
    private final ObjectMapper objectMapper;
    private final EventTypeMapper eventTypeMapper;

    @SneakyThrows
    public <T extends Event> EventWithId<T> appendEvent(@NonNull Event event) {
        List<EventWithId<T>> result = jdbcTemplate.query(
                SqlQueries.INSERT_EVENT_QUERY,
                Map.of(
                        "aggregateId", event.getAggregateId(),
                        "version", event.getVersion(),
                        "eventType", event.getEventType(),
                        "jsonObj", objectMapper.writeValueAsString(event)
                ),
                this::toEvent
        );
        return result.get(0);
    }

    public Collection<EventWithId<Event>> readEvents(@NonNull UUID aggregateId,
                                                     @Nullable Integer fromVersion,
                                                     @Nullable Integer toVersion) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("aggregateId", aggregateId);
        parameters.addValue("fromVersion", fromVersion, Types.INTEGER);
        parameters.addValue("toVersion", toVersion, Types.INTEGER);

        return jdbcTemplate.query(
                SqlQueries.READ_EVENTS_QUERY,
                parameters,
                this::toEvent
        );
    }

    public List<EventWithId<Event>> readEventsAfterCheckpoint(@NonNull String aggregateType,
                                                                    @NonNull BigInteger lastProcessedTransactionId,
                                                                    long lastProcessedEventId) {
        return jdbcTemplate.query(
                SqlQueries.READ_EVENTS_AFTER_CHECKPOINT_QUERY,
                Map.of(
                        "aggregateType", aggregateType,
                        "lastProcessedTransactionId", lastProcessedTransactionId.toString(),
                        "lastProcessedEventId", lastProcessedEventId
                ),
                this::toEvent
        );
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private <T extends Event> EventWithId<T> toEvent(ResultSet rs, int rowNum) {
        long id = rs.getLong("ID");
        String transactionId = rs.getString("TRANSACTION_ID");
        String eventType = rs.getString("EVENT_TYPE");
        PGobject jsonObj = (PGobject) rs.getObject("JSON_DATA");
        String json = jsonObj.getValue();
        Class<? extends Event> eventClass = eventTypeMapper.getClassByEventType(eventType);
        Event event = objectMapper.readValue(json, eventClass);
        return new EventWithId<>(id, new BigInteger(transactionId), (T) event);
    }
}
