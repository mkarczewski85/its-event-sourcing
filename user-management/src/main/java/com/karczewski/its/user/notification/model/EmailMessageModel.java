package com.karczewski.its.user.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageModel {
    private String message;
    private String[] to;
    private String subject;
}
