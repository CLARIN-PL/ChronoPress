package pl.edu.pwr.chrono.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Sentence;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.infrastructure.Time;
import pl.edu.pwr.chrono.readmodel.dto.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

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

    @Override
    public List<WordFrequencyDTO> findWordFrequencyByLexeme(DataSelectionDTO selection) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WordFrequencyDTO> q = cb.createQuery(WordFrequencyDTO.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));
        predicates.add(TextSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));

        q.select(cb.construct(WordFrequencyDTO.class,
                root.get("posLemma"),
                root.get("posAlias"),
                cb.count(root.get("id"))));

        q.groupBy(root.get("posLemma"), root.get("posAlias"));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<WordFrequencyDTO> queryResult = em.createQuery(q).getResultList();
        long size = queryResult.stream().mapToLong(i -> i.getCount()).sum();
        queryResult.forEach(i -> i.setPercentage((i.getCount() * 100.0) / size));
        return queryResult;
    }

    @Override
    public List<WordFrequencyDTO> findWordFrequencyNotLemmatized(DataSelectionDTO selection) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WordFrequencyDTO> q = cb.createQuery(WordFrequencyDTO.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));
        predicates.add(TextSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));

        q.select(cb.construct(WordFrequencyDTO.class,
                root.get("txt"),
                cb.count(root.get("id"))));

        q.groupBy(root.get("txt"));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<WordFrequencyDTO> queryResult = em.createQuery(q).getResultList();
        long size = queryResult.stream().mapToLong(i -> i.getCount()).sum();
        queryResult.forEach(i -> i.setPercentage((i.getCount() * 100.0) / size));
        return queryResult;
    }

    @Override
    public TimeSeriesResult findTimeSeries(final DataSelectionDTO selection, final TimeSeriesDTO dto) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimeProbe> q = cb.createQuery(TimeProbe.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));

        if (dto.getLexeme() != null && dto.getLexeme().size() > 0) {
            predicates.add(WordSpecification.byLexeme(dto.getLexeme()).toPredicate(root, q, cb));
        } else if (!"".equals(dto.getRegularExpression())) {
            predicates.add(WordSpecification.lexemeByRegExp(dto.getRegularExpression()).toPredicate(root, q, cb));
        }
        predicates.add(TextSpecification.search(selection).toPredicate(text, q, cb));

        Expression<Integer> year = cb.function("year", Integer.class, text.get("date"));
        Expression<Integer> month = cb.function("month", Integer.class, text.get("date"));

        if (dto.getUnit() == Time.MONTH) {
            q.select(cb.construct(TimeProbe.class,
                    root.get("posLemma"),
                    year,
                    month,
                    cb.count(text.get("date"))));
            q.groupBy(month, year, root.get("posLemma"));
            q.orderBy(cb.asc(root.get("posLemma")), cb.asc(year), cb.asc(month));
        }

        if (dto.getUnit() == Time.YEAR) {
            q.select(cb.construct(TimeProbe.class,
                    root.get("posLemma"),
                    year, cb.count(text.get("date"))));

            q.groupBy(year, root.get("posLemma"));
            q.orderBy(cb.asc(root.get("posLemma")), cb.asc(year));
        }

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<TimeProbe> queryResult = em.createQuery(q).getResultList();

        Map<String, List<TimeProbe>> sorted = queryResult.stream()
                .collect(groupingBy(TimeProbe::getLexeme, mapping(s -> s, toList())));
        return new TimeSeriesResult(dto.getUnit(), sorted);
    }

    @Override
    public List<ConcordanceDTO> findConcordanceNotLemmatized(DataSelectionDTO selection, String lemma) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConcordanceDTO> q = cb.createQuery(ConcordanceDTO.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));
        predicates.add(TextSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));
        predicates.add(WordSpecification.byText(lemma).toPredicate(root, q, cb));

        q.select(cb.construct(ConcordanceDTO.class,
                root.get("txt"),
                root.get("posLemma"),
                sentence.get("sentPlain"),
                text.get("date"),
                text.get("journalTitle")));

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<ConcordanceDTO> queryResult = em.createQuery(q).getResultList();
        return queryResult;
    }

}
