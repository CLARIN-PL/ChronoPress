package pl.clarin.chronopress.business.importer.boundary;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import pl.clarin.chronopress.business.importer.entity.SampleXml;
import pl.clarin.chronopress.business.sample.boundary.DictionaryWordFonemsAndSyllablesRepository;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.sample.entity.DictionaryWordFonemsAndSyllables;
import pl.clarin.chronopress.business.sample.entity.ProcessingStatus;
import pl.clarin.chronopress.business.sample.entity.Sample;

import javax.ejb.*;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@ApplicationScoped
public class ImportFonemsService {

    private final static String PATH = "/tmp/chronopress/uploads/";

    @Inject
    SampleFacade facade;

    private final AtomicBoolean importRunning = new AtomicBoolean(false);

    public void importFile(String filename) {
        if (!isImportRunning()) {
            importRunning.getAndSet(true);
            try {
                unZipIt(filename);
                loadToDatabase();
            } catch (ZipException ex) {
                Logger.getLogger(ImportFonemsService.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                importRunning.getAndSet(false);
            }
        }
    }

    public void updateWords(){

        List<DictionaryWordFonemsAndSyllables> all = facade.getAllDictionaryWordFonemsAndSyllables();
        final AtomicInteger c = new AtomicInteger();

        all.stream().forEach( d -> {
            facade.updateWordByDictionaryFonem(d);
            System.out.println( c.getAndIncrement() );
        });
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
            Collection<File> files = FileUtils.listFiles(FileUtils.getFile(PATH), new String[]{"csv"}, true);
            files.forEach(f -> {

                try (Stream<String> stream = Files.lines(Paths.get(f.getPath()))) {
                    final AtomicInteger c = new AtomicInteger();
                    stream.forEach( l -> {
                       String[] ll = l.split(";");
                       DictionaryWordFonemsAndSyllables dwfs =  new DictionaryWordFonemsAndSyllables(ll[0],ll[1],ll[2]);
                       facade.saveFonem(dwfs);
                       c.getAndIncrement();
                       System.out.println(dwfs);
                    });
                    System.out.println("Total: " + c.get());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            FileUtils.deleteDirectory(new File("/tmp/chronopress/uploads/"));
        } catch (IOException ex) {
            Logger.getLogger(ImportFonemsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
