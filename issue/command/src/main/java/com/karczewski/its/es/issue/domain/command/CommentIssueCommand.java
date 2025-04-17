package com.karczewski.its.es.issue.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import com.karczewski.its.es.issue.domain.aggregate.IssueAggregate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class CommentIssueCommand extends Command {

    private final String comment;
    private final UUID authoredBy;

    @Builder
    public CommentIssueCommand(UUID aggregateId, String comment, UUID authoredBy) {
        super(IssueAggregate.AGGREGATE_TYPE_NAME, aggregateId);
        this.comment = comment;
        this.authoredBy = authoredBy;
    }

}
