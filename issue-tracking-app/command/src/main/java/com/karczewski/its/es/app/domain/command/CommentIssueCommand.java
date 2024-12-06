package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.app.domain.aggregate.AggregateType;
import com.karczewski.its.es.core.domain.command.Command;
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
        super(AggregateType.ISSUE.toString(), aggregateId);
        this.comment = comment;
        this.authoredBy = authoredBy;
    }

}
