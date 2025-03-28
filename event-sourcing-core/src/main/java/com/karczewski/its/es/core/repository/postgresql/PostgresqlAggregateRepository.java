package com.karczewski.its.es.core.repository.postgresql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.aggregate.AggregateTypeMapper;
import com.karczewski.its.es.core.repository.AggregateRepository;
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

import java.sql.ResultSet;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@Repository
@RequiredArgsConstructor
public class PostgresqlAggregateRepository implements AggregateRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final AggregateTypeMapper aggregateTypeMapper;

    public void createAggregateIfAbsent(@NonNull String aggregateType,
                                        @NonNull UUID aggregateId) {
        jdbcTemplate.update(
                SqlQueries.INSERT_AGGREGATE_QUERY,
                Map.of(
                        SqlParameters.AGGREGATE_ID_PARAM, aggregateId,
                        SqlParameters.AGGREGATE_TYPE_PARAM, aggregateType
                )
        );
    }

    public boolean updateAggregateVersion(@NonNull UUID aggregateId,
                                          int expectedVersion,
                                          int newVersion) {
        int updatedRows = jdbcTemplate.update(
                SqlQueries.UPDATE_AGGREGATE_QUERY,
                Map.of(
                        SqlParameters.NEW_VERSION_PARAM, newVersion,
                        SqlParameters.AGGREGATE_ID_PARAM, aggregateId,
                        SqlParameters.EXPECTED_VERSION_PARAM, expectedVersion
                )
        );
        return updatedRows > 0;
    }

    @SneakyThrows
    public void createAggregateSnapshot(@NonNull Aggregate aggregate) {
        jdbcTemplate.update(
                SqlQueries.INSERT_AGGREGATE_SNAPSHOT_QUERY,
                Map.of(
                        SqlParameters.AGGREGATE_ID_PARAM, aggregate.getAggregateId(),
                        SqlParameters.VERSION_PARAM, aggregate.getVersion(),
                        SqlParameters.JSON_OBJ_PARAM, objectMapper.writeValueAsString(aggregate)
                )
        );
    }

    public Optional<Aggregate> findAggregateSnapshot(@NonNull UUID aggregateId,
                                                     @Nullable Integer version) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(SqlParameters.AGGREGATE_ID_PARAM, aggregateId);
        parameters.addValue(SqlParameters.VERSION_PARAM, version, Types.INTEGER);

        return jdbcTemplate.query(
                SqlQueries.SELECT_AGGREGATE_SNAPSHOT_QUERY,
                parameters,
                this::toAggregate
        ).stream().findFirst();
    }

    @SneakyThrows
    private Aggregate toAggregate(ResultSet rs, int rowNum) {
        String aggregateType = rs.getString("AGGREGATE_TYPE");
        PGobject jsonObj = (PGobject) rs.getObject("JSON_DATA");
        String json = jsonObj.getValue();
        Class<? extends Aggregate> aggregateClass = aggregateTypeMapper.getClassByAggregateType(aggregateType);
        return objectMapper.readValue(json, aggregateClass);
    }
}
