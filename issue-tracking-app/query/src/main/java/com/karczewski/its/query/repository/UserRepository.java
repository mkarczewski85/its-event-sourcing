package com.karczewski.its.query.repository;

import com.karczewski.its.query.entity.User;
import com.karczewski.its.query.entity.UserIssueCount;
import org.springframework.data.domain.Pageable;
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
            SELECT new com.karczewski.its.query.entity.UserIssueCount(u.id, COALESCE(COUNT(i.id), 0))
            FROM UserAccount u
            LEFT JOIN IssueProjection i
            ON i.assignedTo.id = u.id AND i.status IN ('IN_PROGRESS', 'ASSIGNED')
            WHERE u.role = 'TECHNICIAN'
            GROUP BY u.id
            ORDER BY COUNT(i.id) ASC
            """)
    List<UserIssueCount> getUserAssignedIssuesCount(Pageable pageable);
}
