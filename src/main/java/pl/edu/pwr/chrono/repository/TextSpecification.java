package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TextSpecification {

	public static Specification<Text> search(final DataSelectionDTO dto){

		return (root, query, cb) -> {

            List<Predicate> criteriaList = new ArrayList<>();

            if(dto.getYears() !=null && !dto.getYears().isEmpty()){
                criteriaList.add(isPublishedInYear(dto.getYears()).toPredicate(root, query, cb));
            }

            if(dto.getTitles() !=null && !dto.getTitles().isEmpty()){
                criteriaList.add(isInTitle_j(dto.getTitles()).toPredicate(root,query,cb));
            }

            if(dto.getPeriodicType() !=null && !dto.getPeriodicType().isEmpty()){
                criteriaList.add(isInPeriodType(dto.getPeriodicType()).toPredicate(root,query,cb));
            }

            if(dto.getExposition() !=null && !dto.getExposition().isEmpty()){
                criteriaList.add(isInExposition(dto.getExposition()).toPredicate(root,query,cb));
            }

            if(dto.getAuthors() !=null && !dto.getAuthors().isEmpty()){
					criteriaList.add(isLikeAuthor(dto.getAuthors()).toPredicate(root,query,cb));
            }

            return cb.and(criteriaList.toArray(new Predicate[0]));
        };
	}

	public static Specification<Text> isInTitle_j(final Collection<String> titles){
		return (root, query, cb) -> {
            Predicate predicate  = root.get("title_j").in(titles);
            return predicate;
        };
	}

	public static Specification<Text> isInPeriodType(final Collection<String> period){
		return (root, query, cb) -> {
            Predicate predicate  = root.get("period").in(period);
            return predicate;
        };
	}

	public static Specification<Text> isInExposition(final Collection<Integer> exposition){
		return (root, query, cb) -> {
            Predicate predicate  = root.get("exposition").in(exposition);
            return predicate;
        };
	}

	public static Specification<Text> isLikeAuthor(final Collection<String> authors){
		return (root, query, cb) -> {
			List<Predicate> criteriaList = new ArrayList<>();
			Predicate predicate;
			if(authors.size() == 1) {
				 predicate = cb.like(root.get("authors"), getContainsLikePattern(authors.iterator().next()));
			} else {
				authors.forEach(a -> {
					Predicate p = cb.like(root.get("authors"), getContainsLikePattern(a));
					criteriaList.add(p);
				});
				predicate = cb.or(criteriaList.toArray(new Predicate[0]));
			}
			return predicate;
        };
	}

	public static Specification<Text> isInTitle_a(final Collection<String> titles){
		return (root, query, cb) -> {
            Predicate predicate  = root.get("title_a").in(titles);
            return predicate;
        };
	}

	public static Specification<Text> isPublishedInYear(final Collection<Integer> years){
		return (root, query, cb) -> {
            Predicate predicate  = cb.function("year", Integer.class, root.get("date")).in(years);
            return predicate;
        };
	}

	private static String getContainsLikePattern(String searchTerm) {
		if (searchTerm == null || searchTerm.isEmpty()) {
			return "%";
		}
		else {
			return "%" + searchTerm + "%";
		}
	}
}
