package pl.clarin.chronopress.presentation.page.admin.datamanagment;

import com.airhacks.porcupine.execution.boundary.Dedicated;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Notification;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import pl.clarin.chronopress.business.importer.boundary.ImportFonemsService;
import pl.clarin.chronopress.business.importer.boundary.ImportSamplesService;
import pl.clarin.chronopress.business.importer.boundary.ProcessSampleService;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractPresenter;

@UIScoped
public class DataManagementViewPresenter extends AbstractPresenter<DataManagementView> {

    @Inject
    ImportSamplesService importSamplesService;

    @Inject
    ProcessSampleService processSampleService;

    @Inject
    ImportFonemsService importFonemsService;

    @Inject
    @Dedicated
    ExecutorService importerExecutor;

    @Override
    protected void onViewEnter() {
        if (importSamplesService.isImportRunning()) {
            getView().setImportInProgress();
        }
        if (processSampleService.isProcessingRunning()) {
            getView().setProcessingInProgress();
        }
    }

    public void onImportSamplesEvent(@Observes(notifyObserver = Reception.IF_EXISTS) ImportSamplesEvent event) {
        CompletableFuture.supplyAsync(() -> {
            importSamplesService.importFile(event.getPath());
            return 1;
        },
                importerExecutor).thenRun(() -> {
                    getView().uploadingSamplesFinished();
                });
     }

    public void onProcessSamplesFinishedEvent(@Observes(notifyObserver = Reception.IF_EXISTS) ProcessingSamplesFinishedEvent event) {
        getView().proccesingSamplesFinished();
        Notification.show("Procesowanie zakończone", Notification.Type.TRAY_NOTIFICATION);
    }

    public void onProcessingStatusEvent(@Observes(notifyObserver = Reception.IF_EXISTS) ProcessSamplesStatusEvent event){
        getView().setProcessingStatusMessage(event.getMessage());
    }

    public void onImportFonemsEvent(@Observes(notifyObserver = Reception.IF_EXISTS) ImportFonemsEvent event) {
        CompletableFuture.supplyAsync(() -> {
                    importFonemsService.importFile(event.getPath());
                    return 1;
                },
                importerExecutor).thenRun(() -> {
            getView().uploadingFOnemsFinished();
        });
    }
}
