package pl.clarin.chronopress.business.sample.boundary;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import pl.clarin.chronopress.business.audience.boundary.AudienceFacade;
import pl.clarin.chronopress.business.propername.entity.ProperName;
import pl.clarin.chronopress.business.sample.control.MovingAverage;
import pl.clarin.chronopress.business.sample.entity.ProcessingStatus;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.business.sample.entity.Sentence;
import pl.clarin.chronopress.business.sample.entity.SentenceProperName;
import pl.clarin.chronopress.business.sample.entity.Word;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataExplorationForm.PartOfSpeech;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;
import pl.clarin.chronopress.presentation.shered.dto.DataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.FrequencyItem;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.dto.LexemeProfile;
import pl.clarin.chronopress.presentation.shered.dto.SentenceAnalysisDTO;
import pl.clarin.chronopress.presentation.shered.dto.SentenceWordCount;
import pl.clarin.chronopress.presentation.shered.dto.SimpleGeolocation;
import pl.clarin.chronopress.presentation.shered.dto.Time;
import pl.clarin.chronopress.presentation.shered.dto.TimeProbe;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesDTO;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesResult;
import pl.clarin.chronopress.presentation.shered.dto.WordAnalysisDTO;

@Stateless
public class SampleFacade {

    @Inject
    SampleRepository sampleRepository;

    @Inject
    SentenceRepository sentenceRepository;

    @Inject
    AudienceFacade audienceFacade;

    @Inject
    EntityManager em;

    public Sample save(Sample s) {
        return sampleRepository.saveAndFlushAndRefresh(s);
    }

    public Sample findById(Long id) {
        return sampleRepository.findBy(id);
    }

    public List<Sample> findSamplesToProcess() {
        return sampleRepository.findUnProcessedSamples();
    }

    public Sentence saveSentence(Sentence se) {
        return sentenceRepository.saveAndFlushAndRefresh(se);
    }

    public List<String> findAuthors() {
        return sampleRepository.findAuthors();
    }

    public List<String> findPeriods() {
        return sampleRepository.findPeriods();
    }

    public List<Integer> findExpositions() {
        return sampleRepository.findExpositions();
    }

    public List<Integer> findYears() {
        return sampleRepository.findYears();
    }

    public List<String> findJournalTitles() {
        return sampleRepository.findJournalTitles();
    }

    public InitDataSelectionDTO getInitDataSelection() {
        InitDataSelectionDTO dto = new InitDataSelectionDTO();
        dto.setAuthors(findAuthors());
        dto.setExpositions(findExpositions());
        dto.setJournalTitles(findJournalTitles());
        dto.setYears(findYears());
        dto.setPeriods(findPeriods());
        dto.setAudience(audienceFacade.findAudience());
        return dto;
    }

    public long findSamplesCount(String journal, Date publication, String article, String author) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Sample> root = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();

        if (journal != null && !journal.equals("")) {
            predicates.add(cb.like(root.get("journalTitle"), "%" + journal + "%"));
        }
        if (publication != null) {
            predicates.add(cb.equal(root.<Date>get("date"), publication));
        }
        if (article != null && !article.equals("")) {
            predicates.add(cb.like(root.get("articleTitle"), "%" + article + "%"));
        }
        if (author != null && !author.equals("")) {
            predicates.add(cb.like(root.get("authors"), "%" + author + "%"));
        }
        q.select(cb.count(root));
        q.where(predicates.toArray(new Predicate[predicates.size()]));
        return em.createQuery(q).getSingleResult();
    }

    public List<SentenceWordCount> findSentenceWordCountAndWordLength(DataSelectionDTO dto, SentenceAnalysisDTO analysisDTO) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SentenceWordCount> q = cb.createQuery(SentenceWordCount.class);

        Root<Word> root = q.from(Word.class);
        Root<Sentence> sentence = q.from(Sentence.class);
        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(cb.equal(root.get("sentence").get("id"), sentence.get("id")));
        predicates.add(SampleSpecification.search(dto).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));

        if (analysisDTO.getSentenceRegularExpression() != null && !analysisDTO.getSentenceRegularExpression().equals("")) {
            predicates.add(SentenceSpecification.byText(analysisDTO.getSentenceRegularExpression()).toPredicate(sentence, q, cb));
        }

        q.select(cb.construct(SentenceWordCount.class,
                root.get("sentence").get("id"),
                root.get("sentence").get("wordCount"),
                cb.sum(root.get("letterCount"))));

        q.groupBy(root.get("sentence").get("id"), root.get("sentence").get("wordCount"));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(q).getResultList();
    }

    public List<Sample> findSamples(int firstRow, boolean asc, String sortProperty, String journal,
            Date publication, String article, String author) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Sample> q = cb.createQuery(Sample.class);

        String sortField = sortProperty == null ? "id" : sortProperty;

        Root<Sample> root = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();

        if (journal != null && !journal.equals("")) {
            predicates.add(cb.like(root.get("journalTitle"), "%" + journal + "%"));
        }
        if (publication != null) {
            predicates.add(cb.equal(root.<Date>get("date"), publication));
        }
        if (article != null && !article.equals("")) {
            predicates.add(cb.like(root.get("articleTitle"), "%" + article + "%"));
        }
        if (author != null && !author.equals("")) {
            predicates.add(cb.like(root.get("authors"), "%" + author + "%"));
        }

        q.select(root);
        if (asc) {
            q.orderBy(cb.asc(root.get(sortField)));
        } else {
            q.orderBy(cb.desc(root.get(sortField)));
        }

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(q)
                .setFirstResult(firstRow)
                .setMaxResults(30)
                .getResultList();
    }

    public List<Integer> findWordsLengths(DataSelectionDTO selection, WordAnalysisDTO dto) {
        if (dto.getNamingUnit()) {
            return findWordsLengthsForProperNames(selection, dto);
        }
        return findByWordsLengths(selection, dto);
    }

    public List<Integer> findByWordsLengths(DataSelectionDTO selection, WordAnalysisDTO dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> q = cb.createQuery(Integer.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        Predicate p0 = cb.equal(sentence.get("sample").get("id"), text.get("id"));
        Predicate p1 = SampleSpecification.search(selection).toPredicate(text, q, cb);
        Predicate p2 = WordSpecification.filter(dto).toPredicate(root, q, cb);

        q.select(root.get("letterCount"));
        q.where(p0, p1, p2);

        return em.createQuery(q).getResultList();
    }

    public List<Integer> findWordsLengthsForProperNames(DataSelectionDTO selection, WordAnalysisDTO dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> q = cb.createQuery(Integer.class);

        Root<SentenceProperName> root = q.from(SentenceProperName.class);
        Join<SentenceProperName, ProperName> properName = root.join("properName");
        Join<SentenceProperName, Sentence> sentence = root.join("sentence");

        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(SampleSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(cb.equal(properName.get("type"), "nam"));

        q.select(cb.function("length", Integer.class, properName.get("orth")));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(q).getResultList();
    }

    public List<Long> findWordsLengthFrequency(DataSelectionDTO selection, WordAnalysisDTO dto) {
        if (dto.getNamingUnit()) {
            return findWordsFequencyForProperNames(selection, dto);
        }
        return findByWordsLengthFrequency(selection, dto);
    }

    public List<Long> findByWordsLengthFrequency(DataSelectionDTO selection, WordAnalysisDTO dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        Predicate p0 = cb.equal(sentence.get("sample").get("id"), text.get("id"));
        Predicate p1 = SampleSpecification.search(selection).toPredicate(text, q, cb);
        Predicate p2 = WordSpecification.filter(dto).toPredicate(root, q, cb);

        q.select(cb.count(root.get("word")));
        q.groupBy(root.get("word"));
        q.orderBy(cb.asc(root.get("word")));
        q.where(p0, p1, p2);

        return em.createQuery(q).getResultList();
    }

    public List<Long> findWordsFequencyForProperNames(DataSelectionDTO selection, WordAnalysisDTO dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);

        Root<SentenceProperName> root = q.from(SentenceProperName.class);
        Join<SentenceProperName, ProperName> properName = root.join("properName");
        Join<SentenceProperName, Sentence> sentence = root.join("sentence");

        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(SampleSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(cb.equal(properName.get("type"), "nam"));

        q.select(cb.count(properName.get("orth")));
        q.groupBy(properName.get("orth"));
        q.orderBy(cb.asc(properName.get("orth")));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(q).getResultList();
    }

    public List<FrequencyItem> findWordFrequencyByLexeme(DataSelectionDTO selection) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FrequencyItem> q = cb.createQuery(FrequencyItem.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(SampleSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));

        q.select(cb.construct(FrequencyItem.class,
                root.get("lemma"),
                root.get("posAlias"),
                cb.count(root.get("id"))));

        q.groupBy(root.get("lemma"), root.get("posAlias"));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<FrequencyItem> queryResult = em.createQuery(q).getResultList();
        long size = queryResult.stream().mapToLong(i -> i.getValue()).sum();
        queryResult.forEach(i -> i.setPercentage((i.getValue() * 100.0) / size));
        return queryResult;
    }

    public List<FrequencyItem> findWordFrequencyNotLemmatized(DataSelectionDTO selection) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FrequencyItem> q = cb.createQuery(FrequencyItem.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(SampleSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));

        q.select(cb.construct(FrequencyItem.class,
                root.get("word"),
                cb.count(root.get("id"))));

        q.groupBy(root.get("word"));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<FrequencyItem> queryResult = em.createQuery(q).getResultList();
        long size = queryResult.stream().mapToLong(i -> i.getValue()).sum();
        queryResult.forEach(i -> i.setPercentage((i.getValue() * 100.0) / size));
        return queryResult;
    }

    public WordStatistic findWordsInYears() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WordYearlyStatisticItem> q = cb.createQuery(WordYearlyStatisticItem.class);
        Root<Sample> root = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("processingStatus"), ProcessingStatus.PROCESSED));

        Expression<Integer> year = cb.function("year", Integer.class, root.get("date"));
        q.select(cb.construct(WordYearlyStatisticItem.class,
                year,
                cb.sum(root.get("wordCount"))));
        q.groupBy(year);
        q.orderBy(cb.asc(year));
        List<WordYearlyStatisticItem> queryResult = em.createQuery(q).getResultList();

        return new WordStatistic(queryResult);
    }

    public TimeSeriesResult findTimeSeries(final DataSelectionDTO selection, final TimeSeriesDTO dto) {

        WordStatistic normalizer = findWordsInYears();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimeProbe> q = cb.createQuery(TimeProbe.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));

        if (dto.getLexeme() != null && dto.getLexeme().size() > 0) {
            String first = (String) dto.getLexeme().toArray()[0];
            if (first.contains("\"")) {
                final Set<String> words = new HashSet<>();
                dto.getLexeme().forEach(i -> {
                    words.add(i.replace("\"", ""));
                });
                predicates.add(WordSpecification.byTexts(words).toPredicate(root, q, cb));
            } else {
                predicates.add(WordSpecification.byLexemes(dto.getLexeme()).toPredicate(root, q, cb));
            }

        } else if (!"".equals(dto.getRegularExpression())) {
            predicates.add(WordSpecification.lexemeByRegExp(dto.getRegularExpression()).toPredicate(root, q, cb));
        }
        predicates.add(SampleSpecification.search(selection).toPredicate(text, q, cb));

        Expression<Integer> year = cb.function("year", Integer.class, text.get("date"));
        Expression<Integer> month = cb.function("month", Integer.class, text.get("date"));

        if (dto.getUnit() == Time.MONTH) {
            q.select(cb.construct(TimeProbe.class,
                    root.get("lemma"),
                    year,
                    month,
                    cb.count(text.get("date"))));
            q.groupBy(month, year, root.get("lemma"));
            q.orderBy(cb.asc(root.get("lemma")), cb.asc(year), cb.asc(month));
        }

        if (dto.getUnit() == Time.YEAR) {
            q.select(cb.construct(TimeProbe.class,
                    root.get("lemma"),
                    year, cb.count(text.get("date"))));

            q.groupBy(year, root.get("lemma"));
            q.orderBy(cb.asc(root.get("lemma")), cb.asc(year));
        }

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<TimeProbe> queryResult = em.createQuery(q).getResultList();

        Map<String, List<TimeProbe>> sorted = new HashMap<>();

        Map<Integer, Map<Integer, List<TimeProbe>>> groupByDate = queryResult.stream()
                .collect(groupingBy(p -> p.getYear(), groupingBy(p -> p.getMonth())));

        if (dto.getAsSumOfResults()) {
            List<TimeProbe> tmp = Lists.newArrayList();
            groupByDate.forEach((y, map) -> {
                map.forEach((m, t) -> {
                    long cnt = t.stream().mapToLong(o -> o.getCount()).sum();
                    TimeProbe o = new TimeProbe("Suma próbek", y, m, cnt);
                    tmp.add(o);
                });
            });

            Collections.sort(tmp, (Object o1, Object o2) -> {

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
            });
            sorted.put("Suma próbek", tmp);

        } else {

            List<TimeProbe> tmp = Lists.newArrayList();

            groupByDate.forEach((y, map) -> {
                map.forEach((m, t) -> {
                    TimeProbe n = new TimeProbe("", y, m, 0l);
                    t.forEach(i -> {
                        n.setLexeme(i.getLexeme());
                        n.setCount(n.getCount() + i.getCount());
                    });
                    tmp.add(n);
                });
            });

            Collections.sort(tmp, (Object o1, Object o2) -> {

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
            });

            sorted = tmp.stream()
                    .collect(groupingBy(TimeProbe::getLexeme, mapping(s -> s, toList())));
        }

        if (dto.getUnit() == Time.MONTH) {
            sorted.forEach((k, v) -> {
                v.forEach(i -> {
                    i.setCount(normalizer.normalizeMonth(i.getYear(), i.getCount()));
                });
            });
        }

        if (dto.getUnit() == Time.YEAR) {
            sorted.forEach((k, v) -> {
                v.forEach(i -> {
                    i.setCount(normalizer.normalizeYear(i.getYear(), i.getCount()));
                });
            });
        }

        TimeSeriesResult seriesResult = new TimeSeriesResult(dto.getUnit(), sorted);
        if (dto.getMovingAverage()) {
            seriesResult.setMovingAverage(movingAverage(sorted, dto.getMovingAverageWindowSize()));
        }
        return seriesResult;
    }

    private Map<String, List<TimeProbe>> movingAverage(Map<String, List<TimeProbe>> data, Integer windowSize) {
        Map<String, List<TimeProbe>> result = new HashMap<>();
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

    // wyszukanie po tekście po całej frazie może byc wielo wyr
    public List<ConcordanceDTO> findConcordance(DataSelectionDTO selection, String lemma, Boolean caseSensitive) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConcordanceDTO> q = cb.createQuery(ConcordanceDTO.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(SampleSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));

        if (lemma.contains("\"")) {
            String l = lemma.replace("\"", "");
            if (caseSensitive) {
                predicates.add(WordSpecification.byTextIgnoreCaseSensitive(l).toPredicate(root, q, cb));
            } else {
                predicates.add(WordSpecification.byText(l).toPredicate(root, q, cb));
            }
        } else {
            Set<String> lexeme = new HashSet<>(Arrays.asList(lemma.replace("\"", "").toLowerCase()));
            predicates.add(WordSpecification.byLexemes(lexeme).toPredicate(root, q, cb));
        }

        q.select(cb.construct(ConcordanceDTO.class,
                text.get("id"),
                root.get("word"),
                root.get("lemma"),
                sentence.get("sentence"),
                text.get("date"),
                text.get("journalTitle"),
                text.get("articleTitle"),
                text.get("style"),
                text.get("period"),
                text.get("status")
        ));

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<ConcordanceDTO> queryResult = em.createQuery(q)
                .getResultList();
        return queryResult;
    }

    public List<ConcordanceDTO> findConcordanceByLemma(String lemma) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConcordanceDTO> q = cb.createQuery(ConcordanceDTO.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));
        predicates.add(WordSpecification.byLexeme(lemma).toPredicate(root, q, cb));

        q.select(cb.construct(ConcordanceDTO.class,
                text.get("id"),
                root.get("word"),
                root.get("lemma"),
                sentence.get("sentence"),
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

    public List<SimpleGeolocation> findProperNames(DataSelectionDTO selection) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SimpleGeolocation> q = cb.createQuery(SimpleGeolocation.class);

        Root<SentenceProperName> root = q.from(SentenceProperName.class);
        Join<SentenceProperName, ProperName> properName = root.join("properName");
        Join<SentenceProperName, Sentence> sentence = root.join("sentence");

        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(SampleSpecification.search(selection).toPredicate(text, q, cb));
        predicates.add(cb.equal(properName.get("processed"), true));

        q.select(cb.construct(SimpleGeolocation.class,
                properName.get("nameOnMap"),
                properName.get("base"),
                properName.get("lat"),
                properName.get("lon")
        ));
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(q).getResultList();
    }

    public List<LexemeProfile> findLexemeProfile(DataSelectionDTO data, String lemma, PartOfSpeech pos, Integer left, Integer right, Boolean caseSensitive) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Word> q = cb.createQuery(Word.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(SampleSpecification.search(data).toPredicate(text, q, cb));

        if (!lemma.contains("\"")) {
            if (caseSensitive) {
                predicates.add(cb.equal(cb.lower(root.get("lemma")), lemma.replace("\"", "")));
            } else {
                predicates.add(cb.equal(cb.lower(root.get("lemma")), lemma.replace("\"", "").toLowerCase()));
            }
        } else {
            predicates.add(cb.equal(cb.lower(root.get("word")), lemma.replace("\"", "")));
        }
        q.select(root);
        q.where(predicates.toArray(new Predicate[predicates.size()]));

        List<LexemeProfile> results = new ArrayList<>();

        List<Word> words = em.createQuery(q).getResultList();

        List<String> stopList = em.createQuery("SELECT s.name FROM StopList s").getResultList();

        List<String> partOfSpeech = new ArrayList<>();

        if (PartOfSpeech.all == pos.all) {
            partOfSpeech.add(PartOfSpeech.adj.toString());
            partOfSpeech.add(PartOfSpeech.adverb.toString());
            partOfSpeech.add(PartOfSpeech.noun.toString());
            partOfSpeech.add(PartOfSpeech.verb.toString());
        } else {
            partOfSpeech.add(pos.toString());
        }

        String findContextWords = "FROM Word w WHERE w.sentence.id = :sentenceId AND w.posAlias in :pos AND w.seq BETWEEN :left AND :right AND w.lemma NOT IN :stopList";

        words.forEach(w -> {

            List<Word> contextWords = em.createQuery(findContextWords)
                    .setParameter("sentenceId", w.getSentence().getId())
                    .setParameter("pos", partOfSpeech)
                    .setParameter("left", w.getSeq() - left)
                    .setParameter("right", w.getSeq() + right)
                    .setParameter("stopList", stopList)
                    .getResultList();

            contextWords.forEach(cw -> {

                String baseColocat = cw.getLemma().toLowerCase();

                if (!cw.getWord().equals(w.getWord())) {
                    String match = w.getSeq() < cw.getSeq() ? cw.getWord() + "_" + w.getWord() : w.getWord() + "_" + cw.getWord();
                    results.add(new LexemeProfile(baseColocat, match));
                }

            });
        });

        Map<LexemeProfile, Long> count = results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        int totalResults = results.size();

        count.forEach((r, c) -> {
            r.setCount(c);
            r.setPercentage((c * 100f) / (totalResults));
            r.setMatch(r.getMatch() + "(" + c + ")");
        });

        Map<String, LexemeProfile> map = new HashMap<>();
        count.forEach((r, c) -> {
            if (map.containsKey(r.getBaseColocat())) {
                if (!map.get(r.getBaseColocat()).getMatch().contains(r.getMatch())) {
                    String match = map.get(r.getBaseColocat()).getMatch() + " , " + r.getMatch();
                    float proc = map.get(r.getBaseColocat()).getPercentage() + r.getPercentage();
                    map.get(r.getBaseColocat()).setMatch(match);
                    map.get(r.getBaseColocat()).setPercentage(proc);

                }
                map.get(r.getBaseColocat()).setCount(map.get(r.getBaseColocat()).getCount() + r.getCount());
            } else {
                map.put(r.getBaseColocat(), r);
            }
        });

        List<LexemeProfile> list = new ArrayList<>();
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

    public List<ConcordanceDTO> findConcordanceByLemma(String base, LocalDate date) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConcordanceDTO> q = cb.createQuery(ConcordanceDTO.class);

        Root<Word> root = q.from(Word.class);
        Join<Word, Sentence> sentence = root.join("sentence");
        Root<Sample> text = q.from(Sample.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(sentence.get("sample").get("id"), text.get("id")));
        predicates.add(WordSpecification.notPunctuation().toPredicate(root, q, cb));
        predicates.add(WordSpecification.byLexeme(base).toPredicate(root, q, cb));

        if (date != null) {
            predicates.add(cb.equal(cb.function("year", Integer.class, sentence.get("sample").get("date")), date.getYear()));
            predicates.add(cb.equal(cb.function("month", Integer.class, sentence.get("sample").get("date")), date.getMonth().getValue()));
        }

        q.select(cb.construct(ConcordanceDTO.class,
                text.get("id"),
                root.get("word"),
                root.get("lemma"),
                sentence.get("sentence"),
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
}
