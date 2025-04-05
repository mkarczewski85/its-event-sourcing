package com.karczewski.its.es.core.repository.postgresql;

import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.repository.AggregateRepository;
import com.karczewski.its.es.core.repository.postgresql.constants.ParameterNames;
import com.karczewski.its.es.core.repository.postgresql.constants.SqlQueries;
import com.karczewski.its.es.core.repository.postgresql.helpers.JsonMapper;
import com.karczewski.its.es.core.repository.postgresql.helpers.RowMappers;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@Repository
@RequiredArgsConstructor
public class PostgresqlAggregateRepository implements AggregateRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JsonMapper jsonMapper;
    private final RowMappers rowMappers;

    @Override
    public void createAggregateIfAbsent(@NonNull String aggregateType,
                                        @NonNull UUID aggregateId) {
        jdbcTemplate.update(
                SqlQueries.INSERT_AGGREGATE_QUERY,
                insertAggregateParams(aggregateType, aggregateId)
        );
    }

    @Override
    public boolean updateAggregateVersion(@NonNull UUID aggregateId,
                                          int expectedVersion,
                                          int newVersion) {
        int updatedRows = jdbcTemplate.update(
                SqlQueries.UPDATE_AGGREGATE_QUERY,
                updateAggregateVersionParams(aggregateId, expectedVersion, newVersion)
        );
        return updatedRows > 0;
    }

    @Override
    public void createAggregateSnapshot(@NonNull Aggregate aggregate) {
        String json = jsonMapper.toJson(aggregate);
        jdbcTemplate.update(
                SqlQueries.INSERT_AGGREGATE_SNAPSHOT_QUERY,
                createAggregateSnapshotParams(aggregate.getAggregateId(), aggregate.getVersion(), json)
        );
    }

    @Override
    public Optional<Aggregate> findAggregateSnapshot(@NonNull UUID aggregateId,
                                                     @Nullable Integer version) {
        return jdbcTemplate.query(
                SqlQueries.SELECT_AGGREGATE_SNAPSHOT_QUERY,
                findAggregateSnapshotParams(aggregateId, version),
                rowMappers::mapToAggregate
        ).stream().findFirst();
    }

    private static Map<String, Object> insertAggregateParams(String aggregateType, UUID aggregateId) {
        return Map.of(
                ParameterNames.AGGREGATE_ID_PARAM, aggregateId,
                ParameterNames.AGGREGATE_TYPE_PARAM, aggregateType
        );
    }

    private static Map<String, Object> updateAggregateVersionParams(UUID aggregateId, int expectedVersion, int newVersion) {
        return Map.of(
                ParameterNames.NEW_VERSION_PARAM, newVersion,
                ParameterNames.AGGREGATE_ID_PARAM, aggregateId,
                ParameterNames.EXPECTED_VERSION_PARAM, expectedVersion
        );
    }

    private static Map<String, Object> createAggregateSnapshotParams(UUID aggregateId, Integer version, String json) {
        return Map.of(
                ParameterNames.AGGREGATE_ID_PARAM, aggregateId,
                ParameterNames.VERSION_PARAM, version,
                ParameterNames.JSON_OBJ_PARAM, json
        );
    }

    private static MapSqlParameterSource findAggregateSnapshotParams(UUID aggregateId, Integer version) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ParameterNames.AGGREGATE_ID_PARAM, aggregateId);
        parameters.addValue(ParameterNames.VERSION_PARAM, version, Types.INTEGER);
        return parameters;
    }
}
