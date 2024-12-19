package com.karczewski.its.user.repository;

import com.karczewski.its.user.entity.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

}
