package com.karczewski.its.query.repository;

import com.karczewski.its.query.entity.User;
import com.karczewski.its.query.entity.UserIssueCount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID>,
        PagingAndSortingRepository<User, UUID>,
        JpaSpecificationExecutor<User> {

    User getById(UUID id);

    @Query("""
            SELECT new com.karczewski.its.query.entity.UserIssueCount(u.id, COUNT(i.id)) 
            FROM User u LEFT JOIN u.assigned i 
            WHERE i.status IN ('IN_PROGRESS', 'ASSIGNED') 
            GROUP BY u.id
            ORDER BY COUNT(i.id) ASC
            """)
    List<UserIssueCount> getUserAssignedIssuesCount();
}
