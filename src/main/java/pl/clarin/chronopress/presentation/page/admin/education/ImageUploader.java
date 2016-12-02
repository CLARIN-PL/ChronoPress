/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.education;

import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.admin.datamanagment.DataManagementViewImpl;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadError;
import pl.exsio.plupload.PluploadFile;
import pl.exsio.plupload.helper.filter.PluploadFilter;
import pl.exsio.plupload.helper.resize.PluploadImageResize;

@Slf4j
public class ImageUploader extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;

    final Label info = new Label();
    private final Plupload uploader = new Plupload("Wybierz plik", FontAwesome.FILES_O);
    private Image image = new Image();

    final String path = "/tmp/chronopress/uploads/images/";
    private String filename;

    @PostConstruct
    public void init() {

        uploader.setCaption(provider.getProperty("label.education.image"));
        uploader.addStyleName(ValoTheme.BUTTON_TINY);
        uploader.addStyleName(ChronoTheme.BUTTON);
        uploader.addFilter(new PluploadFilter("Obraz", "png, jpg, jpeg, gif, bmp"));
        uploader.setMaxFileSize("5mb");
        uploader.setMaxRetries(5);
        uploader.setMultiSelection(false);
        uploader.setPreventDuplicates(true);

        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException ex) {
            Logger.getLogger(DataManagementViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        uploader.setUploadPath(path);

        uploader.addFileUploadedListener(new Plupload.FileUploadedListener() {
            @Override
            public void onFileUploaded(PluploadFile file) {
                Notification.show("Plik: " + file.getName() + " został wgrany");
                filename = file.getUploadedFileAs(File.class).getName();
                image.setSource(new FileResource(file.getUploadedFileAs(File.class)));
            }
        });

        uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {
            @Override
            public void onUploadProgress(PluploadFile file) {
                info.setValue("Wgrywam plik " + file.getName()
                        + "  " + file.getPercent() + "%");
            }
        });

        uploader.addFilesAddedListener(
                new Plupload.FilesAddedListener() {
            @Override
            public void onFilesAdded(PluploadFile[] files
            ) {
                uploader.start();
            }
        }
        );

        uploader.addUploadCompleteListener(
                new Plupload.UploadCompleteListener() {
            @Override
            public void onUploadComplete() {
                info.setValue("Wygrywanie zakończone!");
            }
        }
        );

        uploader.addErrorListener(
                new Plupload.ErrorListener() {
            @Override
            public void onError(PluploadError error
            ) {
                Notification.show("Wgranie pliku niepowiodło się: "
                        + error.getMessage() + " (" + error.getType() + ")",
                        Notification.Type.ERROR_MESSAGE);
            }
        }
        );

        uploader.addFilter(
                new PluploadFilter("Pliki graficzne", "jpg, png, jpeg, bmp"));
        uploader.setImageResize(
                new PluploadImageResize().setEnabled(true)
                .setCrop(true).setHeight(400).setWidth(300));

        MButton remove = new MButton(provider.getProperty("button.remove.image"))
                .withIcon(FontAwesome.TRASH)
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withListener(event -> {
                    removeImage();
                });

        MHorizontalLayout wrapper = new MHorizontalLayout()
                .withSpacing(true)
                .with(uploader, remove, info);

        MVerticalLayout layout = new MVerticalLayout()
                .withFullWidth()
                .withMargin(false)
                .with(wrapper, image);

        setCompositionRoot(layout);
    }

    public void setImage(String name) {
        this.filename = name;
        File file = new File(path + this.filename);
        if(filename != null && file.exists() && file.isFile()){
            image.setSource(new FileResource(file));
        }
    }

    public void removeImage() {
        try {
            Files.deleteIfExists(Paths.get(path + this.filename));
            filename = null;
            image.setSource(null);
            info.setValue("Plik usunięty");
        } catch (IOException ex) {
            log.debug("Unable to remove image file :", ex);
        }
    }

    public String getFilename() {
        return filename;
    }
    
    public void cleanUp(){
        info.setValue("");
        image.setSource(null);
    }
}
