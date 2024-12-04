package com.karczewski.its.user.repository;


import com.karczewski.its.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long>,
        PagingAndSortingRepository<UserAccount, Long>, JpaSpecificationExecutor<UserAccount> {

    Optional<UserAccount> findByEmailAndIsActive(final String email, boolean active);

    Optional<UserAccount> findByUuid(final UUID uuid);

    UserAccount getByUuid(final UUID uuid);

    @Query(value = "SELECT * FROM user_profiles WHERE role = :role AND is_active = true ORDER BY RAND() LIMIT 1", nativeQuery = true)
    UserAccount getRandomWithRole(@Param("role") String role);

    boolean existsByEmail(final String email);

    void deleteByUuid(UUID uuid);

}
