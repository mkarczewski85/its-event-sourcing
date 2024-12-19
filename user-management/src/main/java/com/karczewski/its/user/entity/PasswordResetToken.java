package com.karczewski.its.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_credentials_id")
    private UserCredentials userCredentials;

    @Column(name = "reset_token")
    private String token;

    @Column(name = "expired_at")
    @Builder.Default
    private Long expiredAt = calculateExpirationDateInMillis();

    public boolean isValid() {
        return expiredAt >= System.currentTimeMillis();
    }

    private static Long calculateExpirationDateInMillis() {
        final Instant instant = LocalDateTime.now().plusHours(24).atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

}
