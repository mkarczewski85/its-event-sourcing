package com.karczewski.its.query.service.specification;

import com.karczewski.its.query.entity.IssueProjection;
import com.karczewski.its.query.service.filters.IssueFilters;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class IssueProjectionSpecification {

    private static final String REPORTED_BY_FIELD_NAME = "reportedBy";
    private static final String ASSIGNED_TO_FIELD_NAME = "assignedTo";
    private static final String ID_FIELD_NAME = "id";
    private static final String STATUS_FIELD_NAME = "status";
    private static final String SEVERITY_FIELD_NAME = "severity";
    private static final String TYPE_FIELD_NAME = "type";
    private static final String TITLE_FIELD_NAME = "title";

    public Specification<IssueProjection> getReportedIssueSpecification(IssueFilters filters) {
        return getIssueSpecification(REPORTED_BY_FIELD_NAME, filters);
    }

    public Specification<IssueProjection> getAssignedIssueSpecification(IssueFilters filters) {
        return getIssueSpecification(ASSIGNED_TO_FIELD_NAME, filters);
    }

    public Specification<IssueProjection> getUnassignedIssuesSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(ASSIGNED_TO_FIELD_NAME));
    }

    private Specification<IssueProjection> getIssueSpecification(String fieldName, IssueFilters filters) {
        return (root, query, criteriaBuilder) -> {
            Collection<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(fieldName), filters.getUser()));
            addFilters(filters, criteriaBuilder, root, predicates);
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addFilters(IssueFilters filters,
                                   CriteriaBuilder criteriaBuilder,
                                   Root<IssueProjection> root,
                                   Collection<Predicate> predicates) {
        if (filters != null) {
            addIdPredicateIfProvided(filters, criteriaBuilder, root, predicates);
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
        if (filters.getSeverity() == null) return;
        Predicate predicate = criteriaBuilder.equal(root.get(SEVERITY_FIELD_NAME), filters.getSeverity());
        predicates.add(predicate);
    }

    private static void addTypePredicateIfProvided(IssueFilters filters,
                                                   CriteriaBuilder criteriaBuilder,
                                                   Root<IssueProjection> root,
                                                   Collection<Predicate> predicates) {
        if (filters.getType() == null) return;
        Predicate predicate = criteriaBuilder.equal(root.get(TYPE_FIELD_NAME), filters.getType());
        predicates.add(predicate);
    }

    private static void addStatusPredicateIfProvided(IssueFilters filters,
                                                     CriteriaBuilder criteriaBuilder,
                                                     Root<IssueProjection> root,
                                                     Collection<Predicate> predicates) {
        if (filters.getStatus() == null) return;
        Predicate predicate = criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), filters.getStatus());
        predicates.add(predicate);
    }

    private static void addIdPredicateIfProvided(IssueFilters filters,
                                                 CriteriaBuilder criteriaBuilder,
                                                 Root<IssueProjection> root,
                                                 Collection<Predicate> predicates) {
        if (filters.getProjectionId() == null) return;
        Predicate predicate = criteriaBuilder.equal(root.get(ID_FIELD_NAME), filters.getProjectionId());
        predicates.add(predicate);
    }

    private static void addTitlePredicateIfProvided(IssueFilters filters,
                                                    CriteriaBuilder criteriaBuilder,
                                                    Root<IssueProjection> root,
                                                    Collection<Predicate> predicates) {
        if (filters.getTitlePhrase() == null) return;
        Predicate predicate = criteriaBuilder.like(root.get(TITLE_FIELD_NAME), "%" + filters.getTitlePhrase() + "%");
        predicates.add(predicate);
    }

}
