package com.karczewski.its.es.core.repository.postgresql;

public final class SqlParameters {

    private SqlParameters() {
    }

    static final String SUBSCRIPTION_NAME_PARAM = "subscriptionName";
    static final String LAST_PROCESSED_TRANSACTION_ID_PARAM = "lastProcessedTransactionId";
    static final String LAST_PROCESSED_EVENT_ID_PARAM = "lastProcessedEventId";
    static final String AGGREGATE_ID_PARAM = "aggregateId";
    static final String VERSION_PARAM = "version";
    static final String EVENT_TYPE_PARAM = "eventType";
    static final String JSON_OBJ_PARAM = "jsonObj";
    static final String FROM_VERSION_PARAM = "fromVersion";
    static final String TO_VERSION_PARAM = "toVersion";
    static final String AGGREGATE_TYPE_PARAM = "aggregateType";
    static final String NEW_VERSION_PARAM = "newVersion";
    static final String EXPECTED_VERSION_PARAM = "expectedVersion";


}
