package com.karczewski.its.es.issue.domain.aggregate.valueobject;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class IssueSeverity {

    private final String value;

    public static final IssueSeverity HIGH = new IssueSeverity("HIGH");
    public static final IssueSeverity MEDIUM = new IssueSeverity("MEDIUM");
    public static final IssueSeverity LOW = new IssueSeverity("LOW");

    private static final Map<String, IssueSeverity> NAME_TO_ISSUE_SEVERITY = Map.of(
            HIGH.getValue(), HIGH,
            MEDIUM.getValue(), MEDIUM,
            LOW.getValue(), LOW
    );

    public boolean hasNameEqualTo(String name) {
        return this.value.equals(name);
    }

    @Nonnull
    public static IssueSeverity getByName(@Nonnull String name) {
        IssueSeverity severity = NAME_TO_ISSUE_SEVERITY.get(name);
        if (severity == null) {
            throw new IllegalArgumentException("Unknown IssueSeverity level: " + name);
        }
        return severity;
    }

}
