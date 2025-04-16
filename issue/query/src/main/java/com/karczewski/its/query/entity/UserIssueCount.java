package com.karczewski.its.query.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class UserIssueCount {

    private final UUID userId;
    private final long issueCount;

}
