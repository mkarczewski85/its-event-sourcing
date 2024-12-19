package com.karczewski.its.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_account_settings")
public class UserAccountSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id", referencedColumnName = "id", nullable = false, unique = true)
    private UserAccount userAccount;

    @Column(name = "issue_notifications")
    private Boolean enableIssueNotifications;

    @Column(name = "comment_notifications")
    private Boolean enableCommentNotifications;

}
