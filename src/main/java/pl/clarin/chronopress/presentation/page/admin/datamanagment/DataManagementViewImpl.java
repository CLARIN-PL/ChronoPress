package pl.clarin.chronopress.presentation.page.admin.datamanagment;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.importer.boundary.StartSampleProcessingEvent;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadError;
import pl.exsio.plupload.PluploadFile;
import pl.exsio.plupload.helper.filter.PluploadFilter;

@CDIView(DataManagementView.ID)
@RolesAllowed({"moderator"})
public class DataManagementViewImpl extends AbstractView<DataManagementViewPresenter> implements DataManagementView {

    @Inject
    Instance<DataManagementViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    javax.enterprise.event.Event<ImportSamplesEvent> importSamples;

    @Inject
    javax.enterprise.event.Event<ImportFonemsEvent> importFonems;

    @Inject
    javax.enterprise.event.Event<StartSampleProcessingEvent> processSamples;
    
    @Inject
    javax.enterprise.event.Event<StartGeolocationProcessingEvent> processGeo;

    private final ProgressBar importProgressBar = new ProgressBar();

    private final ProgressBar importFonemsProgressBar = new ProgressBar();

    private final Label uploadInfo = new Label();
    private final Label proccessingInfo = new Label();
    private final Label proccessingGeoInfo = new Label();

    private final Label importFonemsInfo = new Label();

    private final Plupload uploader = new Plupload("Wybierz plik", FontAwesome.FILES_O);
    private final Plupload uploaderFonemsPack = new Plupload("Wybierz plik", FontAwesome.FILES_O);
    private Button process;

    @PostConstruct
    public void init() {

        importProgressBar.setIndeterminate(true);
        importProgressBar.setVisible(false);

        MVerticalLayout section = new MVerticalLayout()
                .withFullWidth()
                .with(new Title(provider.getProperty("view.admin.data.management.title")),
                        samplesUploader(), processUnProcessedSamples(), processUnProcessedGeolocations(), fonemsUploader());

        MHorizontalLayout layout = new MHorizontalLayout()
                .withFullWidth()
                .withMargin(true)
                .withSpacing(true)
                .with(section)
                .withAlign(section, Alignment.MIDDLE_CENTER);

        setCompositionRoot(layout);
    }

    public HorizontalLayout processUnProcessedSamples() {
        process = new MButton()
                .withCaption(provider.getProperty("view.data.management.process.samples.button"))
                .withStyleName(ValoTheme.BUTTON_SMALL)
                .withListener(l -> {
                    processSamples.fire(new StartSampleProcessingEvent());
                    process.setVisible(false);
                    proccessingInfo.setValue("Przygotowanie procesu ...");
                });

        return new MHorizontalLayout()
                .withFullWidth()
                .withSpacing(true)
                .with(new Label(provider.getProperty("view.data.management.process.samples")), proccessingInfo, process);

    }
    
    public HorizontalLayout processUnProcessedGeolocations() {
        Button proc = new MButton()
                .withCaption(provider.getProperty("view.data.management.process.geolocations.button"))
                .withStyleName(ValoTheme.BUTTON_SMALL)
                .withListener(l -> {
                   processGeo.fire(new StartGeolocationProcessingEvent());
                   proccessingGeoInfo.setValue("Proces uruchomiony ...");
                });

        return new MHorizontalLayout()
                .withFullWidth()
                .withSpacing(true)
                .with(new Label(provider.getProperty("view.data.management.process.geolocations")), proccessingGeoInfo, proc);

    }


    public HorizontalLayout samplesUploader() {

        final String path = "/tmp/chronopress/uploads/";

        uploader.addStyleName(ValoTheme.BUTTON_SMALL);
        uploader.addFilter(new PluploadFilter("Pliki zip", "zip"));

        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException ex) {
            Logger.getLogger(DataManagementViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        uploader.setUploadPath(path);

        uploader.setMaxFileSize("100mb");

        //show notification after file is uploaded
        uploader.addFileUploadedListener((PluploadFile file) -> {
            Notification.show("Plik: " + file.getName() + " został wgrany ");
            importSamples.fire(new ImportSamplesEvent(path + file.getUploadedFileAs(File.class).getName()));
            importProgressBar.setVisible(true);
        });

        //update upload progress
        uploader.addUploadProgressListener((PluploadFile file) -> {
            uploadInfo.setValue(file.getName() + " " + file.getPercent() + "%");
        });

        //autostart the uploader after adding files
        uploader.addFilesAddedListener((PluploadFile[] files) -> {
            uploader.start();
        });

        //notify, when the upload process is completed
        uploader.addUploadCompleteListener(() -> {
            uploadInfo.setValue("Wygrywanie zakończone!");
        });

        //handle errors
        uploader.addErrorListener((PluploadError error) -> {
            Notification.show("Wystąpił błąd: "
                    + error.getMessage() + " (" + error.getType() + ")",
                    Notification.Type.ERROR_MESSAGE);
        });

       return  new MHorizontalLayout()
                .withSpacing(true)
                .with(new Label(provider.getProperty("view.data.management.load.samples")),
                        uploader, uploadInfo, importProgressBar);
    }

    public HorizontalLayout fonemsUploader() {

        final String path = "/tmp/chronopress/uploads/";

        uploaderFonemsPack.addStyleName(ValoTheme.BUTTON_SMALL);
        uploaderFonemsPack.addFilter(new PluploadFilter("Pliki zip", "zip"));

        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException ex) {
            Logger.getLogger(DataManagementViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        uploaderFonemsPack.setUploadPath(path);

        uploaderFonemsPack.setMaxFileSize("100mb");

        //show notification after file is uploaded
        uploaderFonemsPack.addFileUploadedListener((PluploadFile file) -> {
            Notification.show("Plik: " + file.getName() + " został wgrany ");
            importFonems.fire(new ImportFonemsEvent(path + file.getUploadedFileAs(File.class).getName()));
            importFonemsProgressBar.setVisible(true);
        });

        //update upload progress
        uploaderFonemsPack.addUploadProgressListener((PluploadFile file) -> {
            importFonemsInfo.setValue(file.getName() + " " + file.getPercent() + "%");
        });

        //autostart the uploader after adding files
        uploaderFonemsPack.addFilesAddedListener((PluploadFile[] files) -> {
            uploaderFonemsPack.start();
        });

        //notify, when the upload process is completed
        uploaderFonemsPack.addUploadCompleteListener(() -> {
            importFonemsInfo.setValue("Wygrywanie zakończone!");
        });

        //handle errors
        uploaderFonemsPack.addErrorListener((PluploadError error) -> {
            Notification.show("Wystąpił błąd: "
                            + error.getMessage() + " (" + error.getType() + ")",
                    Notification.Type.ERROR_MESSAGE);
        });

        return  new MHorizontalLayout()
                .withSpacing(true)
                .with(new Label("Import fonems"),
                        uploaderFonemsPack, importFonemsInfo, importFonemsProgressBar);
    }

    @Override
    public void uploadingSamplesFinished() {
        getUI().access(() -> {
            importProgressBar.setVisible(false);
            uploader.setVisible(true);
        });
    }

    @Override
    public void uploadingFOnemsFinished(){
        getUI().access(() -> {
            importFonemsProgressBar.setVisible(false);
            uploaderFonemsPack.setVisible(true);
        });
    }

    @Override
    public void proccesingSamplesFinished() {
        getUI().access(() -> {
            process.setVisible(true);
        });
    }
    
    @Override
    public void setProcessingStatusMessage(String msg) {
        getUI().access(() -> {
            proccessingInfo.setValue(msg);
        });
    }

    @Override
    public void setImportInProgress() {
        uploader.setVisible(false);
        uploadInfo.setValue(provider.getProperty("label.import.process.allready.running"));
    }

    @Override
    public void setProcessingInProgress() {
        process.setVisible(false);
        proccessingInfo.setValue(provider.getProperty("label.sample.proccessing.process.allready.running"));
    }

    @Override
    protected DataManagementViewPresenter generatePresenter() {
        return presenter.get();
    }
}
