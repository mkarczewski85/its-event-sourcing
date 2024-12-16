package com.karczewski.its.query.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

import java.util.Collection;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "user_accounts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    @Id
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private UserDepartment department;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    private Collection<IssueProjection> assigned;

    @OneToMany(mappedBy = "reportedBy", fetch = FetchType.LAZY)
    private Collection<IssueProjection> reported;

}
