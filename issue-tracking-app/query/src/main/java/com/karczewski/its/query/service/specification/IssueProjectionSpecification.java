package com.karczewski.its.query.service.specification;

import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.entity.User;
import com.karczewski.its.query.service.filters.IssueFilters;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Component
public class IssueProjectionSpecification {

    private static String REPORTED_BY_FIELD_NAME = "reportedBy";
    private static String ASSIGNED_TO_FIELD_NAME = "assignedTo";
    private static String ID_FIELD_NAME = "id";
    private static String STATUS_FIELD_NAME = "status";
    private static String SEVERITY_FIELD_NAME = "severity";
    private static String TYPE_FIELD_NAME = "type";
    private static String TITLE_FIELD_NAME = "title";

    public Specification<IssueProjection> getReportedIssueSpecification(IssueFilters filters) {
        return getIssueSpecification(filters.userUuid(), REPORTED_BY_FIELD_NAME, filters);
    }

    public Specification<IssueProjection> getAssignedIssueSpecification(IssueFilters filters) {
        return getIssueSpecification(filters.userUuid(), ASSIGNED_TO_FIELD_NAME, filters);
    }

    public Specification<IssueProjection> getUnassignedIssuesSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(ASSIGNED_TO_FIELD_NAME));
    }

    private Specification<IssueProjection> getIssueSpecification(UUID userUuid, String fieldName, IssueFilters filters) {
        return (root, query, criteriaBuilder) -> {
            Collection<Predicate> predicates = new ArrayList<>();
            Join<IssueProjection, User> issueUser = root.join(fieldName);
            predicates.add(criteriaBuilder.equal(issueUser.get("id"), userUuid));
            addFilters(filters, criteriaBuilder, root, predicates);
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addFilters(IssueFilters filters,
                                   CriteriaBuilder criteriaBuilder,
                                   Root<IssueProjection> root,
                                   Collection<Predicate> predicates) {
        if (filters != null) {
            addUuidPredicateIfProvided(filters, criteriaBuilder, root, predicates);
            addStatusPredicateIfProvided(filters, criteriaBuilder, root, predicates);
            addSeverityPredicateIfProvided(filters, criteriaBuilder, root, predicates);
            addTypePredicateIfProvided(filters, criteriaBuilder, root, predicates);
            addTitlePredicateIfProvided(filters, criteriaBuilder, root, predicates);
        }
    }

    private static void addSeverityPredicateIfProvided(IssueFilters filters,
                                                       CriteriaBuilder criteriaBuilder,
                                                       Root<IssueProjection> root,
                                                       Collection<Predicate> predicates) {
        if (filters.severity() == null) return;
        Predicate severityPredicate = criteriaBuilder.equal(root.get(SEVERITY_FIELD_NAME), filters.severity());
        predicates.add(severityPredicate);
    }

    private static void addTypePredicateIfProvided(IssueFilters filters,
                                                   CriteriaBuilder criteriaBuilder,
                                                   Root<IssueProjection> root,
                                                   Collection<Predicate> predicates) {
        if (filters.severity() == null) return;
        Predicate severityPredicate = criteriaBuilder.equal(root.get(TYPE_FIELD_NAME), filters.type());
        predicates.add(severityPredicate);
    }

    private static void addStatusPredicateIfProvided(IssueFilters filters,
                                                     CriteriaBuilder criteriaBuilder,
                                                     Root<IssueProjection> root,
                                                     Collection<Predicate> predicates) {
        if (filters.status() == null) return;
        Predicate statusPredicate = criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), filters.status());
        predicates.add(statusPredicate);
    }

    private static void addUuidPredicateIfProvided(IssueFilters filters,
                                                   CriteriaBuilder criteriaBuilder,
                                                   Root<IssueProjection> root,
                                                   Collection<Predicate> predicates) {
        if (filters.projectionUuid() == null) return;
        Predicate uuidPredicate = criteriaBuilder.equal(root.get(ID_FIELD_NAME), filters.status());
        predicates.add(uuidPredicate);
    }

    private static void addTitlePredicateIfProvided(IssueFilters filters,
                                                    CriteriaBuilder criteriaBuilder,
                                                    Root<IssueProjection> root,
                                                    Collection<Predicate> predicates) {
        if (filters.titlePhrase() == null) return;
        Predicate titlePredicate = criteriaBuilder.like(root.get(TITLE_FIELD_NAME), "%" + filters.titlePhrase() + "%");
        predicates.add(titlePredicate);
    }

}
