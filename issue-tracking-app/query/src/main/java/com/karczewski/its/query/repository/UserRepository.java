package com.karczewski.its.query.repository;

import com.karczewski.its.query.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID>,
        PagingAndSortingRepository<User, UUID>,
        JpaSpecificationExecutor<User> {

    User getById(UUID id);

}
