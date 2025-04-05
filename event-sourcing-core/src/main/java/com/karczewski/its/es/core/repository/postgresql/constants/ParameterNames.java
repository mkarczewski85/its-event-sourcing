package com.karczewski.its.es.core.repository.postgresql.constants;

public final class ParameterNames {

    private ParameterNames() {
    }

    public static final String SUBSCRIPTION_NAME_PARAM = "subscriptionName";
    public static final String LAST_PROCESSED_TRANSACTION_ID_PARAM = "lastProcessedTransactionId";
    public static final String LAST_PROCESSED_EVENT_ID_PARAM = "lastProcessedEventId";
    public static final String AGGREGATE_ID_PARAM = "aggregateId";
    public static final String VERSION_PARAM = "version";
    public static final String EVENT_TYPE_PARAM = "eventType";
    public static final String JSON_OBJ_PARAM = "jsonObj";
    public static final String FROM_VERSION_PARAM = "fromVersion";
    public static final String TO_VERSION_PARAM = "toVersion";
    public static final String AGGREGATE_TYPE_PARAM = "aggregateType";
    public static final String NEW_VERSION_PARAM = "newVersion";
    public static final String EXPECTED_VERSION_PARAM = "expectedVersion";


}
