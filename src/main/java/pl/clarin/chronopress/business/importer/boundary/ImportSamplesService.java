package pl.clarin.chronopress.business.importer.boundary;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import pl.clarin.chronopress.business.importer.entity.SampleXml;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.sample.entity.ProcessingStatus;
import pl.clarin.chronopress.business.sample.entity.Sample;

@ApplicationScoped
@TransactionManagement(value = TransactionManagementType.BEAN)
public class ImportSamplesService {

    private final static String PATH = "/tmp/chronopress/uploads/";

    @Inject
    SampleFacade sampleFacade;

    private final AtomicBoolean importRunning = new AtomicBoolean(false);

    public void importFile(String filename) {
        if (!isImportRunning()) {
            importRunning.getAndSet(true);
            try {
                unZipIt(filename);
                loadToDatabase();
            } catch (ZipException ex) {
                Logger.getLogger(ImportSamplesService.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                importRunning.getAndSet(false);
            }
        }
    }

    public boolean isImportRunning() {
        return importRunning.get();
    }

    private void unZipIt(String file) throws ZipException {
        ZipFile zipFile = new ZipFile(file);
        zipFile.extractAll(PATH);
    }

    private void loadToDatabase() {
        try {
            Collection<File> files = FileUtils.listFiles(FileUtils.getFile(PATH), new String[]{"xml"}, true);
            files.forEach(f -> {
                if (validate(f)) {
                    SampleXml sample = unmarshall(f);
                    if (sample != null) {
                        saveSample(sample, f.getName());
                    }
                }
            });

            FileUtils.deleteDirectory(new File("/tmp/chronopress/uploads/"));
        } catch (IOException ex) {
            Logger.getLogger(ImportSamplesService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private SampleXml unmarshall(File f) {
        SampleXml sample = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SampleXml.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            sample = (SampleXml) jaxbUnmarshaller.unmarshal(f);
        } catch (JAXBException ex) {
            Logger.getLogger(ImportSamplesService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sample;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void saveSample(SampleXml s, String filename) {
        Sample sample = new Sample();

        if (s.getTitle_article() != null) {
            sample.setArticleTitle(s.getTitle_article());
        }

        if (s.getNewspaper() != null && s.getNewspaper().getNewspaperTitles() != null) {
            final StringBuilder paper = new StringBuilder();
            s.getNewspaper().getNewspaperTitles().forEach(t -> {
                paper.append(t.getValue()).append(", ");
            });
            sample.setJournalTitle(paper.toString().substring(0, paper.lastIndexOf(", ")));
        }

        if (s.getDate() != null) {
            sample.setDate(s.getDate());
        }

        if (s.getExposition() != null) {
            try {
                Integer p = Integer.parseInt(s.getExposition());
                sample.setExposition(p);
            } catch (NumberFormatException ex) {
                Logger.getLogger(ImportSamplesService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (s.getLanguage() != null) {
            sample.setLang(s.getLanguage().trim());
        }

        if (s.getPeriod() != null) {
            sample.setPeriod(s.getPeriod().trim());
        }

        sample.setTxt(s.getText().trim());

        if (s.getSupport() != null) {
            sample.setMedium(s.getSupport().trim());
        }
        if (s.getStyle() != null) {
            sample.setStyle(s.getStyle().trim());
        }
        if (s.getStatus() != null) {
            sample.setStatus(s.getStatus().trim());
        }

        sample.setFileName(filename);
        sample.setProcessingStatus(ProcessingStatus.TO_PROCESS);

        if (s.getAuthors() != null && s.getAuthors().getAuthors() != null) {
            StringBuilder sb = new StringBuilder();
            s.getAuthors().getAuthors().forEach(a -> {
                sb.append(a.getValue().trim()).append(", ");
            });
            if (!sb.equals("'', ")) {
                sample.setAuthors(sb.substring(0, sb.length() - 2));
            }
        }
        sampleFacade.save(sample);
    }

    private boolean validate(File file) {

        // Try the validation, we assume that if there are any issues with the validation
        // process that the input is invalid.
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            ClassLoader classLoader = getClass().getClassLoader();
            Source schemaFile = new StreamSource(new File(classLoader.getResource("schema.xsd").getFile()));
            Source xmlSource = new StreamSource(file);
            Schema schema = factory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlSource);
        } catch (SAXException ex) {
            Logger.getLogger(ImportSamplesService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(ImportSamplesService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception | Error ex) {
            Logger.getLogger(ImportSamplesService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }
}
