package pl.clarin.chronopress.business.importer.boundary;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import pl.clarin.chronopress.business.importer.control.EngineInvoker;
import pl.clarin.chronopress.business.importer.entity.SentenceXml;
import pl.clarin.chronopress.business.importer.entity.SentencesXml;
import pl.clarin.chronopress.business.importer.entity.WordXml;
import pl.clarin.chronopress.business.propername.boundary.ProperNameFacade;
import pl.clarin.chronopress.business.propername.entity.ProperName;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.sample.entity.ProcessingStatus;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.business.sample.entity.Sentence;
import pl.clarin.chronopress.business.sample.entity.Word;
import pl.clarin.chronopress.presentation.page.admin.datamanagment.ProcessSamplesStatusEvent;
import pl.clarin.chronopress.presentation.page.admin.datamanagment.ProcessingSamplesFinishedEvent;

@ApplicationScoped
@TransactionManagement(value = TransactionManagementType.BEAN)
public class ProcessSampleService {

    public static final String PATH_PROCESS = "/tmp/chronopress/to-process/";
    public static final String ZIP_PROCESS = "/tmp/chronopress/to-process.zip";
    public static final String PATH_RESULTS = "/tmp/chronopress/results/";

    @Inject
    SampleFacade sampleFacade;

    @Inject
    ProperNameFacade properNameFacade;

    @Inject
    EngineInvoker engine;

    @Inject
    javax.enterprise.event.Event<ProcessSamplesStatusEvent> statusEvent;

    @Inject
    javax.enterprise.event.Event<ProcessingSamplesFinishedEvent> finishEvent;

    @Inject
    @Dedicated
    ExecutorService executor;

    private final AtomicInteger numberOfActiveTask = new AtomicInteger(0);

    private final AtomicBoolean processingRunning = new AtomicBoolean(false);

    public void onProcessSamples(@Observes StartSampleProcessingEvent event) {
        if (!isProcessingRunning()) {
            processingRunning.getAndSet(true);
            List<Sample> unProcessed = sampleFacade.findSamplesToProcess();

            new File(PATH_PROCESS).mkdirs();
            new File(PATH_RESULTS).mkdirs();

            unProcessed.forEach(s -> {

                try (PrintWriter writer = new PrintWriter(PATH_PROCESS + s.getId() + ".txt", "UTF-8")) {
                    writer.println(s.getTxt());
                    writer.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ProcessSampleService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            ZipFile zipFile;
            try {
                zipFile = new ZipFile(ZIP_PROCESS);
                ZipParameters parameters = new ZipParameters();

                // set compression method to store compression
                parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

                // Set the compression level
                parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

                Collection<File> files = FileUtils.listFiles(FileUtils.getFile(PATH_PROCESS), new String[]{"txt"}, true);
                zipFile.createZipFile(new ArrayList<>(files), parameters);
                FileUtils.deleteDirectory(new File(PATH_PROCESS));

                engine.liner2(ZIP_PROCESS, PATH_RESULTS);
                loadResultsToDb();
                statusEvent.fire(new ProcessSamplesStatusEvent("Przetwarzanie zlecone na kolejkę"));
            } catch (IOException | ZipException ex) {
                Logger.getLogger(ProcessSampleService.class.getName()).log(Level.SEVERE, null, ex);
                statusEvent.fire(new ProcessSamplesStatusEvent("Wystąpił błąd podczas procesowania !"));
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcessSampleService.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                FileUtils.deleteQuietly(new File(ZIP_PROCESS));
                hasRunningTasks();
            }
        }
    }

    private void hasRunningTasks() {
        if (numberOfActiveTask.get() == 0) {
            statusEvent.fire(new ProcessSamplesStatusEvent("Przetwarzanie zakończone !"));
            finishEvent.fire(new ProcessingSamplesFinishedEvent());
            processingRunning.getAndSet(false);
        }
    }

    public boolean isProcessingRunning() {
        return processingRunning.get();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void loadResultsToDb() {
        Collection<File> files = FileUtils.listFiles(FileUtils.getFile(PATH_RESULTS), new String[]{"txt"}, true);

        List<List<File>> partitoned = Lists.partition(new ArrayList(files), 1000);

        statusEvent.fire(new ProcessSamplesStatusEvent(("Ładowanie danych do bazy")));

        final Map<String, ProperName> nam = new ConcurrentHashMap<>();
        final Map<String, ProperName> nam_geo = new ConcurrentHashMap<>();

        partitoned.forEach(l -> {
            String nr = "" + numberOfActiveTask.get();
            CompletableFuture.supplyAsync(() -> {
                processFiles(l, nr, nam, nam_geo);
                return 0;
            },
                    executor).thenRun(() -> {
                        Logger.getLogger(ProcessSampleService.class.getName()).log(Level.INFO, "Process #{0} zako\u0144czony ", numberOfActiveTask.get());
                        numberOfActiveTask.getAndDecrement();
                        hasRunningTasks();
                    });

            numberOfActiveTask.getAndIncrement();
        });
    }

    public void processFiles(List<File> files, String processNumber, final Map<String, ProperName> nam, final Map<String, ProperName> nam_geo) {
        files.forEach(f -> {
            try {
                SentencesXml xml = unMarshall(f);
                String id = f.getName().replaceAll(".txt", "");

                Sample sample = sampleFacade.findById(new Long(id));
                sample.setWordCount(0);

                if (xml.getSentences() != null) {
                    transformSentenceXml(xml.getSentences(), sample, nam, nam_geo);
                    sample.setProcessingStatus(ProcessingStatus.PROCESSED);
                    sampleFacade.save(sample);
                }

            } catch (JAXBException ex) {
                Logger.getLogger(ProcessSampleService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void transformSentenceXml(final List<SentenceXml> list, final Sample sample, final Map<String, ProperName> nam, final Map<String, ProperName> nam_geo) {

        int sentenceSeqOrder = 0;

        for (SentenceXml s : list) {
            Sentence sentence = new Sentence();
            sentence.setSample(sample);
            sentence.setSeq(sentenceSeqOrder);
            if (s.getWords() != null) {
                transformWordsXml(s.getWords(), sentence);
            }

            Sentence saved = sampleFacade.saveSentence(sentence);
            sample.setWordCount(sample.getWordCount() + sentence.getWordCount());
            sentenceSeqOrder++;

            if (s.getProperNames() != null) {
                properNameFacade.saveSentenceProperName(s.getProperNames(), saved, nam, nam_geo);
            }
        }
    }

    private void transformWordsXml(final List<WordXml> list, final Sentence sentence) {

        List<Word> words = new ArrayList<>();
        int wordSeqOrder = 0;
        int wc = 0;
        StringBuilder sb = new StringBuilder();

        for (WordXml w : list) {
            Word word = new Word();
            word.setWord(w.getOrth());
            word.setLemma(w.getBase());
            word.setPartOfSpeach(w.getCtag());
            word.setLetterCount(w.getOrth().length());
            word.setSentence(sentence);
            word.setSeq(wordSeqOrder);
            buildSentenceFromWords(sb, w.getOrth());
            words.add(word);
            if (!"interp".equals(w.getCtag())) {
                wc++;
            }
            wordSeqOrder++;
        }

        sentence.setWordCount(wc);
        sentence.setSentence(sb.toString().trim());
        sentence.setWords(words);
    }

    private void buildSentenceFromWords(final StringBuilder sb, final String orth) {
        if (orth.equals(",") || orth.equals(".") || orth.equals("?")
                || orth.equals("'") || orth.equals(";") || orth.equals(":")) {
            sb.append(orth);
        } else {
            sb.append(" ").append(orth);

        }
    }

    private SentencesXml unMarshall(File f) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(SentencesXml.class
        );
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SentencesXml sentences = (SentencesXml) jaxbUnmarshaller.unmarshal(f);

        return sentences;
    }
}
