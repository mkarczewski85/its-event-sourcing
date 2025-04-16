package com.karczewski.its.query.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

import java.util.UUID;

@Entity
@Immutable
@Table(name = "attachments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IssueAttachment {

    @Id
    private Long id;
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "name")
    private String name;
    @Column(name = "content_type")
    private String contentType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private IssueProjection issue;
}
