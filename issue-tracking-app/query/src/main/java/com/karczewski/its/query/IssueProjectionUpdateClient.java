package com.karczewski.its.query;

import com.karczewski.its.query.model.IssueCommentModel;
import com.karczewski.its.query.model.IssueProjectionUpdateModel;

public interface IssueProjectionUpdateClient {

    void updateIssueProjection(IssueProjectionUpdateModel model);

    void addIssueComment(IssueCommentModel model);

}
