package pl.clarin.chronopress.business.sample.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.Predicate;
import pl.clarin.chronopress.business.sample.entity.Word;
import pl.clarin.chronopress.business.shered.Specification;
import pl.clarin.chronopress.presentation.shered.dto.WordAnalysisDTO;

public class WordSpecification {

    public static Specification<Word> filter(final WordAnalysisDTO dto) {

        return (root, query, cb) -> {

            List<Predicate> criteriaList = new ArrayList<>();
            criteriaList.add(notPunctuation().toPredicate(root, query, cb));

            if (dto.getAllPartsOfSpeech()) {
                criteriaList.add(allPartsOfSpeech().toPredicate(root, query, cb));
            } else {

                List<Predicate> sub = new ArrayList<>();
                if (dto.getNoun()) {
                    sub.add(isNoun().toPredicate(root, query, cb));
                }
                if (dto.getAdjective()) {
                    sub.add(isAdj().toPredicate(root, query, cb));
                }
                if (dto.getAdverb()) {
                    sub.add(isAdv().toPredicate(root, query, cb));
                }
                if (dto.getVerb()) {
                    sub.add(isVerb().toPredicate(root, query, cb));
                }
                if (sub.size() > 0) {
                    criteriaList.add(cb.or(sub.toArray(new Predicate[sub.size()])));
                }
                if (dto.getWordRegularExpression() != null && !dto.getWordRegularExpression().equals("")) {
                    criteriaList.add(byText(dto.getWordRegularExpression()).toPredicate(root, query, cb));
                }
            }
            return cb.and(criteriaList.toArray(new Predicate[criteriaList.size()]));
        };
    }

    public static Specification<Word> notPunctuation() {
        return (root, query, cb) -> cb.not(root.get("posAlias").in("punct", "interp"));
    }

    public static Specification<Word> isVerb() {
        return (root, query, cb) -> cb.equal(root.get("posAlias"), "verb");
    }

    public static Specification<Word> allPartsOfSpeech() {
        return (root, query, cb) -> root.get("posAlias").in("verb", "noun", "adj", "adv");
    }

    public static Specification<Word> isNoun() {
        return (root, query, cb) -> cb.equal(root.get("posAlias"), "noun");
    }

    public static Specification<Word> isAdj() {
        return (root, query, cb) -> cb.equal(root.get("posAlias"), "adj");
    }

    public static Specification<Word> isAdv() {
        return (root, query, cb) -> cb.equal(root.get("posAlias"), "adv");
    }

    public static Specification<Word> byLexemes(Set<String> lexeme) {
        return (root, query, cb) -> cb.lower(root.get("lemma")).in(lexeme);
    }

    public static Specification<Word> byLexeme(String lexeme) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("lemma")), lexeme);
    }

    public static Specification<Word> lexemeByRegExp(String expr) {
        return (root, query, cb) -> cb.like(root.get("lemma"), expr);
    }

    public static Specification<Word> byText(String expression) {
        return (root, query, cb) -> cb.equal(root.get("word"), expression);
    }

    public static Specification<Word> byTexts(Set<String> words) {
        return (root, query, cb) -> root.get("word").in(words);
    }

    public static Specification<Word> byTextIgnoreCaseSensitive(String expression) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("word")), expression.toLowerCase());
    }
}
