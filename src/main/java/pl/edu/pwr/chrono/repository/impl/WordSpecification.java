package pl.edu.pwr.chrono.repository.impl;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WordSpecification {

    public static Specification<Word> filter(final QuantitativeAnalysisDTO dto) {

        return (root, query, cb) -> {

            List<Predicate> criteriaList = new ArrayList<>();
            criteriaList.add(notPunctuation().toPredicate(root, query, cb));

            if (dto.getAllPartsOfSpeech()) {
                criteriaList.add(allPartsOfSpeech().toPredicate(root, query, cb));
            } else {

                if (dto.getNoun()) {
                    criteriaList.add(isNoun().toPredicate(root, query, cb));
                }
                if (dto.getAdjective()) {
                    criteriaList.add(isAdj().toPredicate(root, query, cb));
                }
                if (dto.getAdverb()) {
                    criteriaList.add(isAdv().toPredicate(root, query, cb));
                }
                if (dto.getVerb()) {
                    criteriaList.add(isVerb().toPredicate(root, query, cb));
                }
                if (dto.getWordRegularExpression() != null && !dto.getWordRegularExpression().equals("")) {
                    criteriaList.add(byText(dto.getWordRegularExpression()).toPredicate(root, query, cb));
                }
            }
            return cb.and(criteriaList.toArray(new Predicate[criteriaList.size()]));
        };
    }

    public static Specification<Word> notPunctuation(){
        return (root, query, cb) -> cb.notEqual(root.get("pos_alias"), "punct");
    }

    public static Specification<Word> isVerb() {
        return (root, query, cb) -> cb.equal(root.get("pos_alias"), "verb");
    }

    public static Specification<Word> allPartsOfSpeech() {
        return (root, query, cb) -> root.get("pos_alias").in("verb", "noun", "adj", "adv");
    }

    public static Specification<Word> isNoun(){
        return (root, query, cb) -> cb.equal(root.get("pos_alias"), "noun");
    }

    public static Specification<Word> isAdj(){
        return (root, query, cb) -> cb.equal(root.get("pos_alias"), "adj");
    }

    public static Specification<Word> isAdv(){
        return (root, query, cb) -> cb.equal(root.get("pos_alias"), "adv");
    }

    public static Specification<Word> byLexeme(Set<String> lexeme) {
        return (root, query, cb) -> root.get("pos_lemma").in(lexeme);
    }

    public static Specification<Word> byText(String expression) {
        return (root, query, cb) -> cb.like(root.get("txt"), expression);
    }
}
