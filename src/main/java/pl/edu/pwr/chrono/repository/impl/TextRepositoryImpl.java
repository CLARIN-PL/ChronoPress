package pl.edu.pwr.chrono.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Sentence;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.readmodel.dto.SentenceWordCount;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextRepositoryImpl implements pl.edu.pwr.chrono.repository.TextRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public Optional<DataSelectionResult> countSamplesByCriteria(DataSelectionDTO dto) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);


        Predicate p0 = cb.equal(sentence.get("text").get("id"), text.get("id"));
        Predicate p1 = TextSpecification.search(dto).toPredicate(text, q, cb);
        Predicate p2 = WordSpecification.notPunctuation().toPredicate(root, q, cb);

        q.select(cb.count(root.get("id")));
        q.groupBy(text.get("id"));
        q.where(p0, p1, p2);

        List<Long> queryResult = em.createQuery(q).getResultList();

        DataSelectionResult result = new DataSelectionResult(Integer.toUnsignedLong(queryResult.size()),
                queryResult.stream().mapToLong(Long::longValue).sum());
        return Optional.of(result);
    }

    @Override
    public List<SentenceWordCount> findSentenceWordCountAndWordLength(DataSelectionDTO dto, QuantitativeAnalysisDTO analysisDTO) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SentenceWordCount> q = cb.createQuery(SentenceWordCount.class);

        Root<Word> root = q.from(Word.class);
        Root<Sentence> sentence = q.from(Sentence.class);
        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));
        predicates.add(cb.equal(root.get("sentence").get("id"), sentence.get("id")));
        predicates.add(TextSpecification.search(dto).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));

        if (analysisDTO.getSentenceRegularExpression() != null && !analysisDTO.getSentenceRegularExpression().equals("")) {
            predicates.add(SentenceSpecification.byText(analysisDTO.getSentenceRegularExpression()).toPredicate(sentence, q, cb));
        }

        q.select(cb.construct(SentenceWordCount.class,
                root.get("sentence").get("id"),
                cb.count(root.get("sentence").get("id")),
                cb.sum(cb.function("length", Long.class, root.get("txt")))));
        q.groupBy(root.get("sentence").get("id"));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(q).getResultList();
    }

    @Override
    public List<Word> findWords(DataSelectionDTO selection, QuantitativeAnalysisDTO dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Word> q = cb.createQuery(Word.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        Predicate p0 = cb.equal(sentence.get("text").get("id"), text.get("id"));
        Predicate p1 = TextSpecification.search(selection).toPredicate(text, q, cb);
        Predicate p2 = WordSpecification.filter(dto).toPredicate(root, q, cb);

        q.select(root);
        q.where(p0, p1, p2);

        return em.createQuery(q).getResultList();
    }
}
