package com.karczewski.its.es.core.repository.postgresql.helpers;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventSubscriptionCheckpoint;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.repository.postgresql.constants.ColumnNames;
import com.karczewski.its.es.core.service.AggregateClassResolver;
import com.karczewski.its.es.core.service.EventClassResolver;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public final class RowMapper {

    private final JsonMapper jsonMapper;
    private final EventClassResolver eventClassResolver;
    private final AggregateClassResolver aggregateClassResolver;

    public Aggregate mapToAggregate(ResultSet resultSet, int rowNum) throws SQLException {
        String aggregateType = resultSet.getString(ColumnNames.AGGREGATE_TYPE_COLUMN);
        PGobject jsonObj = (PGobject) resultSet.getObject(ColumnNames.JSON_DATA_COLUMN);
        String json = jsonObj.getValue();
        Class<? extends Aggregate> aggregateClass = aggregateClassResolver.getAggregateClass(aggregateType);
        return jsonMapper.fromJson(json, aggregateClass);
    }

    public  <T extends Event> EventWithId<T> mapEventWithId(ResultSet resultSet, int rowNum) throws SQLException {
        long id = resultSet.getLong(ColumnNames.ID_COLUMN);
        String transactionId = resultSet.getString(ColumnNames.TRANSACTION_ID_COLUMN);
        String eventType = resultSet.getString(ColumnNames.EVENT_TYPE_COLUMN);
        PGobject jsonObj = (PGobject) resultSet.getObject(ColumnNames.JSON_DATA_COLUMN);
        String json = jsonObj.getValue();
        Class<? extends Event> eventClass = eventClassResolver.getClassByEventType(eventType);
        Event event = jsonMapper.fromJson(json, eventClass);
        return new EventWithId<>(id, new BigInteger(transactionId), (T) event);
    }

    public EventSubscriptionCheckpoint mapEventSubscriptionCheckpoint(ResultSet rs, int rowNum) throws SQLException {
        String lastProcessedTransactionId = rs.getString(ColumnNames.LAST_TRANSACTION_ID_COLUMN);
        long lastProcessedEventId = rs.getLong(ColumnNames.LAST_EVENT_ID_COLUMN);
        return new EventSubscriptionCheckpoint(new BigInteger(lastProcessedTransactionId), lastProcessedEventId);
    }

}
