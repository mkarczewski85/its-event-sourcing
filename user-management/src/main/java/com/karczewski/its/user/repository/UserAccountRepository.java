package com.karczewski.its.user.repository;


import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long>,
        PagingAndSortingRepository<UserAccount, Long>, JpaSpecificationExecutor<UserAccount> {

    Optional<UserAccount> findByEmailAndIsActive(String email, boolean active);

    Optional<UserAccount> findByUuid(UUID uuid);

    UserAccount getByUuid(UUID uuid);

    boolean existsByEmail(String email);
    
    boolean existsByUuidAndRoleAndActive(UUID uuid, UserRole userRole, boolean active);

    void deleteByUuid(UUID uuid);

}
