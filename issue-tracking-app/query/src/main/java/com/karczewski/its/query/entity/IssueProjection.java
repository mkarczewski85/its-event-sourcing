package com.karczewski.its.query.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "issue_projections")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class IssueProjection {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "text", name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "severity", nullable = false)
    private String severity;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Builder.Default
    private Collection<IssueComment> comments = new ArrayList<>(0);

    private IssueProjection(UUID id) {
        this.id = id;
    }

    public static IssueProjection newProjection(UUID uuid) {
        return new IssueProjection(uuid);
    }

    public void addComment(IssueComment issueComment) {
        this.comments.add(issueComment);
        issueComment.setIssue(this);
    }

    public boolean hasPermissionToQuery(UUID userId) {
        if (this.reportedBy != null && this.reportedBy.getId().equals(userId)) {
            return true;
        }
        return this.assignedTo != null && this.assignedTo.getId().equals(userId);
    }

}
