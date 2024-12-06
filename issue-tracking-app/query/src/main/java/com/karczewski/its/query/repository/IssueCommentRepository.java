package com.karczewski.its.query.repository;

import com.karczewski.its.query.entity.IssueComment;
import com.karczewski.its.query.entity.IssueProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueCommentRepository extends CrudRepository<IssueComment, Long>,
        PagingAndSortingRepository<IssueComment, Long>,
        JpaSpecificationExecutor<IssueComment> {

    Page<IssueComment> findAllByIssue(IssueProjection issue, Pageable pageable);
}
