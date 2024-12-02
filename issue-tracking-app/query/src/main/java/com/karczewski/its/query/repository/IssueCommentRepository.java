package com.karczewski.its.query.repository;

import com.karczewski.its.query.entity.IssueComment;
import com.karczewski.its.query.entity.IssueProjection;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IssueCommentRepository extends CrudRepository<IssueComment, Long>,
        PagingAndSortingRepository<IssueComment, Long>,
        JpaSpecificationExecutor<IssueComment> {

    Collection<IssueComment> findAllByIssue(IssueProjection issue);
}
