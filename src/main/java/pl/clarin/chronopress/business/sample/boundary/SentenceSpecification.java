package pl.clarin.chronopress.business.sample.boundary;

import pl.clarin.chronopress.business.sample.entity.Sentence;
import pl.clarin.chronopress.business.shered.Specification;

public class SentenceSpecification {

    public static Specification<Sentence> byText(String expression) {
        return (root, query, cb) -> cb.like(root.get("sentence"), expression);
    }
}
