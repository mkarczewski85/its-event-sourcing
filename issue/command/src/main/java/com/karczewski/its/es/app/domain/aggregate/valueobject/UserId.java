package com.karczewski.its.es.app.domain.aggregate.valueobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public final class UserId {

    private final UUID id;

}
