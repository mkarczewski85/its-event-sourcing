package com.karczewski.its.query;

import com.karczewski.its.query.dto.IssueCommentDto;
import com.karczewski.its.query.dto.IssueProjectionDto;

public interface IssueProjectionClient {

    void updateIssueProjection(IssueProjectionDto dto);

    void addIssueComment(IssueCommentDto dto);

}
