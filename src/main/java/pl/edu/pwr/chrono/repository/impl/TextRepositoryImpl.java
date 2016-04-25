package pl.edu.pwr.chrono.repository.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.application.service.impl.MovingAverage;
import pl.edu.pwr.chrono.application.util.WordToCllDTO;
import pl.edu.pwr.chrono.domain.*;
import pl.edu.pwr.chrono.infrastructure.Time;
import pl.edu.pwr.chrono.readmodel.dto.*;
import pl.edu.pwr.chrono.webui.ui.dataanalyse.DataExplorationTab;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Map<String, List<TimeProbe>> sorted = Maps.newHashMap();
        if(dto.getAsSumOfResults()) {
            Map<Integer, Map<Integer, List<TimeProbe>>> groupByDate =   queryResult.stream()
                    .collect(groupingBy(p -> p.getYear(), groupingBy(p -> p.getMonth())));

            List<TimeProbe> tmp = Lists.newArrayList();
            groupByDate.forEach((y, map ) ->{
                map.forEach((m, t) -> {
                    long cnt = t.stream().mapToLong(o->o.getCount()).sum();
                    TimeProbe o = new TimeProbe("Suma próbek",y,m,cnt);
                    tmp.add(o);
                } );
            });

            Collections.sort(tmp, new Comparator() {

                        public int compare(Object o1, Object o2) {

                            Integer x1 = ((TimeProbe) o1).getYear();
                            Integer x2 = ((TimeProbe) o2).getYear();
                            Integer sComp = x1.compareTo(x2);

                            if (sComp != 0) {
                                return sComp;
                            } else {
                                Integer z1 = ((TimeProbe) o1).getMonth();
                                Integer z2 = ((TimeProbe) o2).getMonth();
                                return z1.compareTo(z2);
                            }
                        }});
            sorted.put("Sum próbek", tmp);

        } else {
            sorted = queryResult.stream()
                    .collect(groupingBy(TimeProbe::getLexeme, mapping(s -> s, toList())));
        }

        TimeSeriesResult seriesResult = new TimeSeriesResult(dto.getUnit(), sorted);
        if (dto.getMovingAverage()) {
            seriesResult.setMovingAverage(movingAverage(sorted, dto.getMovingAverageWindowSize()));
        }
        return seriesResult;
    }

    private Map<String, List<TimeProbe>> movingAverage(Map<String, List<TimeProbe>> data, Integer windowSize) {
        Map<String, List<TimeProbe>> result = Maps.newHashMap();
        data.forEach((s, timeProbes) -> {
            MovingAverage average = new MovingAverage(windowSize);
            List<TimeProbe> probes = Lists.newArrayList();
            timeProbes.forEach(p -> {
                average.add(p.getCount());
                probes.add(new TimeProbe(p.getLexeme(), p.getYear(), p.getMonth(), average.getAverage()));
            });
            result.put(s, probes);
        });
        return result;
    }

    @Override
    public List<ConcordanceDTO> findConcordance(DataSelectionDTO selection, String lemma) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConcordanceDTO> q = cb.createQuery(ConcordanceDTO.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));
        predicates.add(TextSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));

        if (lemma.contains("\"")) {
            Set<String> lexeme = new HashSet<>(Arrays.asList(lemma.replace("\"", "")));
            predicates.add(WordSpecification.byLexeme(lexeme).toPredicate(root, q, cb));
        } else {
            predicates.add(WordSpecification.byText(lemma).toPredicate(root, q, cb));
        }

        q.select(cb.construct(ConcordanceDTO.class,
                text.get("id"),
                root.get("txt"),
                root.get("posLemma"),
                sentence.get("sentPlain"),
                text.get("date"),
                text.get("journalTitle"),
                text.get("articleTitle"),
                text.get("style"),
                text.get("period"),
                text.get("status")
        ));

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<ConcordanceDTO> queryResult = em.createQuery(q).getResultList();
        return queryResult;
    }


    @Override
    public List<WordToCllDTO> findWordCCL(Integer textId) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WordToCllDTO> q = cb.createQuery(WordToCllDTO.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));
        predicates.add(cb.equal(text.get("id"), textId));

        q.select(cb.construct(WordToCllDTO.class,
                text.get("fileName"),
                root.get("txt"),
                root.get("posLemma"),
                root.get("partOfSpeech"),
                sentence.get("id")
        ));

        q.where(predicates.toArray(new Predicate[predicates.size()]));
        q.orderBy(cb.asc(sentence.get("id")), cb.asc(root.get("id")));

        List<WordToCllDTO> queryResult = em.createQuery(q).getResultList();
        return queryResult;
    }

    @Override
    public List<SimpleGeolocation> findProperNames(DataSelectionDTO selection) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SimpleGeolocation> q = cb.createQuery(SimpleGeolocation.class);

        Root<SentenceProperName> root = q.from(SentenceProperName.class);
        Join<SentenceProperName, ProperName> properName = root.join("properName");
        Join<SentenceProperName ,Sentence> sentence = root.join("sentence");

        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));
        predicates.add(TextSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(cb.equal(properName.get("processed"), true));

        q.select(cb.construct(SimpleGeolocation.class,
                properName.get("alias"),
                properName.get("lat"),
                properName.get("lon")
        ));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(q).getResultList();
    }

    @Override
    public List<LexemeProfile> findLexemeProfile(DataSelectionDTO data, String lemma,
                                                 DataExplorationTab.PartOfSpeech pos, Integer left, Integer right){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Word> q = cb.createQuery(Word.class);

        Root<Word> root = q.from(Word.class);
        Join<Word ,Sentence> sentence = root.join("sentence");
        Root<Text> text = q.from(Text.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("text").get("id"), text.get("id")));
        predicates.add(TextSpecification.search(data).toPredicate(text, q, cb));
        predicates.add(cb.equal(root.get("txt"), lemma));

        q.select(root);
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<LexemeProfile> results = Lists.newArrayList();

        List<Word> words = em.createQuery(q).getResultList();

        List<String> stopList = em.createQuery("SELECT s.name FROM StopList s").getResultList();

        words.forEach(w -> {
            String findContextWords = "FROM Word w WHERE w.sentence.id = :sentenceId AND w.posAlias = :pos  AND w.seq BETWEEN :left AND :right AND w.posLemma NOT IN :stopList";
            List<Word> contextWords =  em.createQuery(findContextWords)
                    .setParameter("sentenceId", w.getSentence().getId())
                    .setParameter("pos", pos.toString())
                    .setParameter("left", w.getSeq() - left)
                    .setParameter("right", w.getSeq() + right)
                    .setParameter("stopList", stopList)
                    .getResultList();

            contextWords.forEach(cw -> {

                String baseColocat = cw.getPosLemma().toLowerCase();
                String match = w.getSeq() < cw.getSeq() ? cw.getTxt() + "_" + w.getTxt() : w.getTxt() + "_" + cw.getTxt();
                results.add(new LexemeProfile(baseColocat, match));

            });
        });

        Map<LexemeProfile, Long> count =  results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        int totalResults = results.size();

        count.forEach((r, c) ->  {
            r.setCount(c);
            r.setPercentage((c*100f)/(totalResults));
            r.setMatch(r.getMatch() + "("+c+")");
        });

        Map<String, LexemeProfile> map = Maps.newHashMap();
        count.forEach((r, c) -> {
            if (map.containsKey(r.getBaseColocat())) {
                if (!map.get(r.getBaseColocat()).getMatch().contains(r.getMatch())) {
                    String match = map.get(r.getBaseColocat()).getMatch() + " , " + r.getMatch();
                    float proc =  map.get(r.getBaseColocat()).getPercentage()  + r.getPercentage();
                    map.get(r.getBaseColocat()).setMatch(match);
                    map.get(r.getBaseColocat()).setPercentage(proc);

                }
                map.get(r.getBaseColocat()).setCount(map.get(r.getBaseColocat()).getCount() + r.getCount());
            } else {
                map.put(r.getBaseColocat(), r);
            }
        });

        List<LexemeProfile> list = Lists.newArrayList();
        map.forEach((k, v) -> list.add(v));
        Collections.sort(list, new Comparator() {

           public int compare(Object o1, Object o2) {
                Long x1 = ((LexemeProfile) o1).getCount();
                Long x2 = ((LexemeProfile) o2).getCount();
                return x1.compareTo(x2);
            }
        });

        return list;
    }
}
