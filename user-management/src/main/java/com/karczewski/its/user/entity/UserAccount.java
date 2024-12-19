package com.karczewski.its.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private UserCredentials userCredentials;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private UserAccountSettings userAccountSettings;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public void deactivate() {
        this.isActive = false;
    }

}
