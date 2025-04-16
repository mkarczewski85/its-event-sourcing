package com.karczewski.its.query.repository;

import com.karczewski.its.query.entity.IssueProjection;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IssueProjectionRepository extends CrudRepository<IssueProjection, UUID>,
        PagingAndSortingRepository<IssueProjection, UUID>,
        JpaSpecificationExecutor<IssueProjection> {

}
