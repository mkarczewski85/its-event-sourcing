package com.karczewski.its.es.core.repository.postgresql.constants;

public final class SqlQueries {

    private SqlQueries() {
    }

    public static final String READ_EVENTS_QUERY = """
            SELECT ID,
                   TRANSACTION_ID::text,
                   EVENT_TYPE,
                   JSON_DATA
              FROM ES_EVENT
             WHERE AGGREGATE_ID = :aggregateId
               AND (:fromVersion IS NULL OR VERSION > :fromVersion)
               AND (:toVersion IS NULL OR VERSION <= :toVersion)
             ORDER BY VERSION ASC
            """;

    public static final String INSERT_EVENT_QUERY = """
            INSERT INTO ES_EVENT (TRANSACTION_ID, AGGREGATE_ID, VERSION, EVENT_TYPE, JSON_DATA)
            VALUES(pg_current_xact_id(), :aggregateId, :version, :eventType, :jsonObj::json)
            RETURNING ID, TRANSACTION_ID::text, EVENT_TYPE, JSON_DATA
            """;

    public static final String READ_EVENTS_AFTER_CHECKPOINT_QUERY = """
            SELECT e.ID,
                   e.TRANSACTION_ID::text,
                   e.EVENT_TYPE,
                   e.JSON_DATA
              FROM ES_EVENT e
              JOIN ES_AGGREGATE a on a.ID = e.AGGREGATE_ID
             WHERE a.AGGREGATE_TYPE = :aggregateType
               AND (e.TRANSACTION_ID, e.ID) > (:lastProcessedTransactionId::xid8, :lastProcessedEventId)
               AND e.TRANSACTION_ID < pg_snapshot_xmin(pg_current_snapshot())
             ORDER BY e.TRANSACTION_ID ASC, e.ID ASC
            """;

    public static final String INSERT_AGGREGATE_QUERY = """
            INSERT INTO ES_AGGREGATE (ID, VERSION, AGGREGATE_TYPE)
            VALUES (:aggregateId, 0, :aggregateType)
            ON CONFLICT DO NOTHING
            """;

    public static final String UPDATE_AGGREGATE_QUERY = """
            UPDATE ES_AGGREGATE
               SET VERSION = :newVersion
             WHERE ID = :aggregateId
               AND VERSION = :expectedVersion
            """;

    public static final String INSERT_AGGREGATE_SNAPSHOT_QUERY = """
            INSERT INTO ES_AGGREGATE_SNAPSHOT (AGGREGATE_ID, VERSION, JSON_DATA)
            VALUES (:aggregateId, :version, :jsonObj::json)
            """;

    public static final String SELECT_AGGREGATE_SNAPSHOT_QUERY = """
            SELECT a.AGGREGATE_TYPE,
                   s.JSON_DATA
              FROM ES_AGGREGATE_SNAPSHOT s
              JOIN ES_AGGREGATE a ON a.ID = s.AGGREGATE_ID
             WHERE s.AGGREGATE_ID = :aggregateId
               AND (:version IS NULL OR s.VERSION <= :version)
             ORDER BY s.VERSION DESC
             LIMIT 1
            """;

    public static final String INSERT_EVENT_SUBSCRIPTION_QUERY = """
            INSERT INTO ES_EVENT_SUBSCRIPTION (SUBSCRIPTION_NAME, LAST_TRANSACTION_ID, LAST_EVENT_ID)
            VALUES (:subscriptionName, '0'::xid8, 0)
            ON CONFLICT DO NOTHING
            """;

    public static final String SELECT_EVENT_SUBSCRIPTION_QUERY = """
            SELECT LAST_TRANSACTION_ID::text,
                   LAST_EVENT_ID
              FROM ES_EVENT_SUBSCRIPTION
             WHERE SUBSCRIPTION_NAME = :subscriptionName
               FOR UPDATE SKIP LOCKED
            """;

    public static final String UPDATE_EVENT_SUBSCRIPTION_QUERY = """
            UPDATE ES_EVENT_SUBSCRIPTION
               SET LAST_TRANSACTION_ID = :lastProcessedTransactionId::xid8,
                   LAST_EVENT_ID = :lastProcessedEventId
             WHERE SUBSCRIPTION_NAME = :subscriptionName
            """;
}
