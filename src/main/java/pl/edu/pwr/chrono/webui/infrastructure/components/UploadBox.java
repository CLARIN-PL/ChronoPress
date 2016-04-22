package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.infrastructure.Operation;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.io.*;

@SpringComponent
@ViewScope
@Slf4j
public class UploadBox extends CustomComponent implements Receiver,
        ProgressListener, FailedListener, SucceededListener, StartedListener {

    private static final long serialVersionUID = -46336015006190050L;

    @Autowired
    private DbPropertiesProvider provider;

    // Put upload in this memory buffer that grows automatically
    final ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
    final ProgressBar progress = new ProgressBar(0.0f);

    final long UPLOAD_LIMIT = 10000000l;
    // Name of the uploaded file
    private String filename;
    private String destination;
    private Upload upload;

    public void setDestination(String destination){
        this.destination = destination;
    }
    public UploadBox() {

        // Create the upload component and handle all its events
        upload = new Upload();
        upload.setReceiver(this);
        upload.addProgressListener(this);
        upload.addFailedListener(this);
        upload.addSucceededListener(this);
        upload.addStartedListener(this);
        upload.setWidth("100%");
        // Put the upload and image display in a panel
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.addComponent(upload);
        wrapper.setWidth("100%");
        wrapper.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
        content.addComponent(wrapper);

        content.addComponent(progress);

        progress.setVisible(true);
        progress.setWidth("100%");

        setCompositionRoot(content);
    }

    public OutputStream receiveUpload(String filename, String mimeType) {
        this.filename = filename;
        os.reset(); // Needed to allow re-uploading
        return os;
    }

    @Override
    public void updateProgress(long readBytes, long contentLength) {
        if (readBytes > UPLOAD_LIMIT) {
            Notification.show(provider.getProperty("error.file.too.large"), Notification.Type.ERROR_MESSAGE);
            upload.interruptUpload();
        }
        progress.setVisible(true);
        if (contentLength == -1)
            progress.setIndeterminate(true);
        else {
            progress.setIndeterminate(false);
            progress.setValue(((float) readBytes) / ((float) contentLength));
        }
    }

    private Operation operation;

    public void setOperationAfterUpload(Operation op){
        this.operation = op;
    }

    public void uploadSucceeded(SucceededEvent event) {

        StreamSource source = new StreamSource() {
            private static final long serialVersionUID = -4905654404647215809L;

            public InputStream getStream() {
                return new ByteArrayInputStream(os.toByteArray());
            }
        };
        FileOutputStream fos  = null;
        try {
            fos = new FileOutputStream(destination+filename, false);
            IOUtils.copy(source.getStream(),fos);
        } catch (IOException e) {
            log.debug("Saving uploaded file failed", e);
        }
        operation.execute(destination+filename);
    }

    @Override
    public void uploadFailed(FailedEvent event) {
        Notification.show(provider.getProperty("error.file.upload.failed"), Notification.Type.ERROR_MESSAGE);
    }

    @Override
    public void uploadStarted(StartedEvent event) {
        progress.setValue(0f);
        if (event.getContentLength() > UPLOAD_LIMIT) {
            Notification.show(provider.getProperty("error.file.too.large"), Notification.Type.ERROR_MESSAGE);
            upload.interruptUpload();
        }
    }
}
