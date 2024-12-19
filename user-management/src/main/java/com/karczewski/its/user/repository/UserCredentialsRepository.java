package com.karczewski.its.user.repository;

import com.karczewski.its.user.entity.UserCredentials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends CrudRepository<UserCredentials, Long> {
}
