package com.karczewski.its.attachments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "attachments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AttachmentUpload {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    @Column(name = "uuid", nullable = false)
    private UUID uuid = UUID.randomUUID();
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "content_type", nullable = false)
    private String contentType;
    @Lob
    @Column(name = "data", columnDefinition = "BYTEA")
    private byte[] data;
    @Column(name = "issue_id", nullable = false)
    private UUID issueId;

}
