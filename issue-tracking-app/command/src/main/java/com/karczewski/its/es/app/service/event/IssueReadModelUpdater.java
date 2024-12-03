package com.karczewski.its.es.app.service.event;

import com.karczewski.its.es.app.domain.aggregate.AggregateType;
import com.karczewski.its.es.app.domain.aggregate.IssueAggregate;
import com.karczewski.its.es.app.domain.event.IssueCommentedEvent;
import com.karczewski.its.es.core.domain.aggregate.Aggregate;
import com.karczewski.its.es.core.domain.event.Event;
import com.karczewski.its.es.core.domain.event.EventWithId;
import com.karczewski.its.es.core.service.event.SyncEventHandler;
import com.karczewski.its.query.IssueProjectionUpdateClient;
import com.karczewski.its.query.dto.IssueCommentDto;
import com.karczewski.its.query.dto.IssueProjectionDto;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class IssueReadModelUpdater implements SyncEventHandler {

    private final IssueProjectionUpdateClient issueProjectionClient;

    @Override
    public void handleEvents(List<EventWithId<Event>> events, Aggregate aggregate) {
        log.debug("Updating read model for an issue {}", aggregate);
        IssueProjectionDto issueProjectionDto = mapToDto(CastingUtility.safeCast(aggregate, IssueAggregate.class));
        issueProjectionClient.updateIssueProjection(issueProjectionDto);
        events.stream()
                .map(EventWithId::event)
                .filter(event -> event.isTypeOf(IssueCommentedEvent.class.getTypeName()))
                .map(event -> mapToDto(CastingUtility.safeCast(event, IssueCommentedEvent.class)))
                .forEach(issueProjectionClient::addIssueComment);
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return AggregateType.ISSUE.toString();
    }

    private static IssueProjectionDto mapToDto(IssueAggregate aggregate) {
        return IssueProjectionDto.builder()
                .uuid(aggregate.getAggregateId())
                .title(aggregate.getIssueTitle().value())
                .description(aggregate.getIssueDescription().value())
                .status(aggregate.getIssueStatus().getValue())
                .type(aggregate.getIssueType().getValue())
                .severity(aggregate.getIssueSeverity().getValue())
                .reportedAt(aggregate.getReportedAt())
                .reportedBy(aggregate.getReportedBy().getId())
                .assignedTo(aggregate.getAssignedTo().getId())
                .build();
    }

    private static IssueCommentDto mapToDto(IssueCommentedEvent event) {
        return IssueCommentDto.builder()
                .issueUuid(event.getAggregateId())
                .content(event.getComment())
                .authoredBy(event.getAuthoredBy())
                .publishedAt(event.getCreatedDate())
                .build();
    }
}
