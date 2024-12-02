package com.karczewski.its.es.app.domain.command;

import com.karczewski.its.es.core.domain.command.Command;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class CommentIssueCommand extends Command {

    private final String comment;
    private final String authoredBy;

    @Builder
    public CommentIssueCommand(String aggregateType, UUID aggregateId, String comment, String authoredBy) {
        super(aggregateType, aggregateId);
        this.comment = comment;
        this.authoredBy = authoredBy;
    }

    public UUID getAuthoredBy() {
        return UUID.fromString(authoredBy);
    }

}
