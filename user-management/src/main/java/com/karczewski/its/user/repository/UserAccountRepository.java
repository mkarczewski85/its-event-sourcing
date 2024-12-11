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
public interface UserAccountRepository extends CrudRepository<UserAccount, UUID>,
        PagingAndSortingRepository<UserAccount, UUID>, JpaSpecificationExecutor<UserAccount> {

    Optional<UserAccount> findByEmail(String email);

    boolean existsByEmail(String email);
    
    boolean existsByIdAndRole(UUID id, UserRole userRole);

}
