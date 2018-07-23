package pl.clarin.chronopress.business.sample.boundary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.criteria.Predicate;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.business.shered.Specification;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;

public class SampleSpecification {

    public static Specification<Sample> search(final DataSelectionDTO dto) {

        return (root, query, cb) -> {

            List<Predicate> criteriaList = new ArrayList<>();

            if (dto.getYears() != null && !dto.getYears().isEmpty()) {
                criteriaList.add(isPublishedInYear(dto.getYears()).toPredicate(root, query, cb));
            }

            if (dto.getTitles() != null && !dto.getTitles().isEmpty()) {
                criteriaList.add(isJournalTitle(dto.getTitles()).toPredicate(root, query, cb));
            }

            if (dto.getPeriodicType() != null && !dto.getPeriodicType().isEmpty()) {
                criteriaList.add(isInPeriodType(dto.getPeriodicType()).toPredicate(root, query, cb));
            }

            if (dto.getExposition() != null && !dto.getExposition().isEmpty()) {
                criteriaList.add(isInExposition(dto.getExposition()).toPredicate(root, query, cb));
            }

            if (dto.getAudience() != null && !dto.getAudience().isEmpty()) {
                criteriaList.add(inAudience(dto.getAudience()).toPredicate(root, query, cb));
            }

            if (dto.getAuthors() != null && !dto.getAuthors().isEmpty()) {
                criteriaList.add(inAuthor(dto.getAuthors()).toPredicate(root, query, cb));
            }

            return cb.and(criteriaList.toArray(new Predicate[criteriaList.size()]));
        };
    }

    public static Specification<Sample> isJournalTitle(final Collection<String> titles) {
        return (root, query, cb) -> root.get("journalTitle").in(titles);
    }

    public static Specification<Sample> isInPeriodType(final Collection<String> period) {
        return (root, query, cb) -> root.get("period").in(period);
    }

    public static Specification<Sample> isInExposition(final Collection<Integer> exposition) {
        return (root, query, cb) -> root.get("exposition").in(exposition);
    }

    public static Specification<Sample> inAudience(final Collection<String> audience) {
        return (root, query, cb) -> root.get("journalTitle").in(audience);
    }

    public static Specification<Sample> inAuthor(final Collection<String> authors) {
        return (root, query, cb) -> {
            return root.get("authors").in(authors);
        };
    }

    public static Specification<Sample> isArticleTitle(final Collection<String> titles) {
        return (root, query, cb) -> root.get("articleTitle").in(titles);
    }

    public static Specification<Sample> isPublishedInYear(final Collection<Integer> years) {
        return (root, query, cb) -> cb.function("year", Integer.class, root.get("date")).in(years);
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm + "%";
        }
    }
}
