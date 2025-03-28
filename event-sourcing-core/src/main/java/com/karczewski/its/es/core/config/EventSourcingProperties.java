package com.karczewski.its.es.core.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "event-sourcing")
public class EventSourcingProperties {

    private static final SnapshottingProperties SNAPSHOTTING_PROPERTIES = new SnapshottingProperties(false, 10);

    @Valid
    @NestedConfigurationProperty
    private Map<String, SnapshottingProperties> snapshotting = new HashMap<>();

    public SnapshottingProperties getSnapshottingProperties(String aggregateType) {
        return snapshotting.getOrDefault(aggregateType, SNAPSHOTTING_PROPERTIES);
    }

    public record SnapshottingProperties(
            boolean enabled,
            @Min(2)
            int nthEvent
    ) {
    }
}
