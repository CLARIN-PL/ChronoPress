package pl.edu.pwr.chrono.repository.impl;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.pwr.chrono.domain.Sentence;

public class SentenceSpecification {

    public static Specification<Sentence> byText(String expression) {
        return (root, query, cb) -> cb.like(root.get("sentPlain"), expression);
    }
}
