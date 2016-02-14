package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pwr.chrono.domain.Sentence;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.infrastructure.Time;
import pl.edu.pwr.chrono.readmodel.UCTimeSeries;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.TimeProbe;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesDTO;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesResult;
import pl.edu.pwr.chrono.repository.impl.TextSpecification;
import pl.edu.pwr.chrono.repository.impl.WordSpecification;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
public class DefaultUCTimeSeries implements UCTimeSeries {

    @Autowired
    private EntityManager em;

    @Autowired
    private ListeningExecutorService service;

    @Override
    @Transactional(readOnly = true)
    public ListenableFuture<TimeSeriesResult> calculate(final DataSelectionDTO selection, final TimeSeriesDTO dto) {
        return service.submit(() -> fetch(selection, dto));
    }

    public TimeSeriesResult fetch(final DataSelectionDTO selection, final TimeSeriesDTO dto) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimeProbe> q = cb.createQuery(TimeProbe.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        Predicate p0 = cb.equal(sentence.get("text").get("id"), text.get("id"));
        Predicate p1 = WordSpecification.byLexeme(dto.getLexeme()).toPredicate(root, q, cb);
        Predicate p2 = TextSpecification.search(selection).toPredicate(text, q, cb);

        Expression<Integer> year = cb.function("year", Integer.class, text.get("date"));
        Expression<Integer> month = cb.function("month", Integer.class, text.get("date"));

        if (dto.getUnit() == Time.MONTH) {
            q.select(cb.construct(TimeProbe.class,
                    root.get("pos_lemma"),
                    year,
                    month,
                    cb.count(text.get("date"))));
            q.groupBy(month, year, root.get("pos_lemma"));
            q.orderBy(cb.asc(root.get("pos_lemma")), cb.asc(year), cb.asc(month));
        }

        if (dto.getUnit() == Time.YEAR) {
            q.select(cb.construct(TimeProbe.class,
                    root.get("pos_lemma"),
                    year, cb.count(text.get("date"))));

            q.groupBy(year, root.get("pos_lemma"));
            q.orderBy(cb.asc(root.get("pos_lemma")), cb.asc(year));
        }

        q.where(p0, p1, p2);

        List<TimeProbe> queryResult = em.createQuery(q).getResultList();

        Map<String, List<TimeProbe>> sorted = queryResult.stream()
                .collect(groupingBy(TimeProbe::getLexeme, mapping(s -> s, toList())));

        return new TimeSeriesResult(dto.getUnit(), sorted);
    }

}
