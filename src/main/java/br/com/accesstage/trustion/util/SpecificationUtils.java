package br.com.accesstage.trustion.util;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static br.com.accesstage.trustion.util.Utils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SpecificationUtils {

    private static final String LIKE = "%%%s%%";

    public static void like(CriteriaBuilder builder, Root root, List<Predicate> predicates, String field, String value) {
        if (isNotBlank(value)) {
            String like = String.format(LIKE, value.toLowerCase());
            predicates.add(builder.like(builder.lower(root.<String>get(field)), like));
        }
    }

    public static void equal(CriteriaBuilder builder, Root root, List<Predicate> predicates, String field, Long value) {
        if (value != null && value > 0L) {
            predicates.add(builder.equal(root.<String>get(field), value));
        }
    }

    public static void equal(CriteriaBuilder builder, Root root, List<Predicate> predicates, String field, String value) {
        if (isNotBlank(value)) {
            predicates.add(builder.equal(builder.lower(root.<String>get(field)), value.toLowerCase()));
        }
    }

    public static void in(CriteriaBuilder builder, Root root, List<Predicate> predicates, String field, List value) {
        if (isNotEmpty(value)) {
            predicates.add(root.get(field).in(value));
        }
    }

    public static void greaterThanOrEqualTo(CriteriaBuilder builder, Root root, List<Predicate> predicates, String field, Date value) {
        if (value != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(field), value));
        }
    }

    public static void greaterThanOrEqualTo(CriteriaBuilder builder, Root root, List<Predicate> predicates, String field, LocalDateTime value) {
        if (value != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.<LocalDateTime>get(field), value));
        }
    }

    public static void lessThanOrEqualTo(CriteriaBuilder builder, Root root, List<Predicate> predicates, String field, Date value) {
        if (value != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(field), value));
        }
    }

    public static void lessThanOrEqualTo(CriteriaBuilder builder, Root root, List<Predicate> predicates, String field, LocalDateTime value) {
        if (value != null) {
            predicates.add(builder.lessThanOrEqualTo(root.<LocalDateTime>get(field), value));
        }
    }

    public static Predicate and(CriteriaBuilder builder, List<Predicate> predicates) {
        return builder.and(predicates.toArray(new Predicate[]{}));
    }
}
