package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.*;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesResult;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
@Slf4j
public class DataAnalysisPresenter extends Presenter<DataAnalysisView> {

    @Autowired
    private UCDataSelection ucDataSelection;

    @Autowired
    private UCLoadingYears ucLoadingYears;

    @Autowired
    private UCLoadingTitles ucLoadingTitles;

    @Autowired
    private UCLoadingPeriods ucLoadingPeriods;

    @Autowired
    private UCLoadingExpositions ucLoadingExpositions;

    @Autowired
    private UCLoadingAuthors ucLoadingAuthors;

    @Autowired
    private UCQuantitativeAnalysis ucQuantitativeAnalysis;

    @Autowired
    private UCTimeSeries ucTimeSeries;

    public void executeQuantitativeCalculations() {
        try {
            Futures.addCallback(ucQuantitativeAnalysis.calculate(
                    view.getDataSelectionPanel().getData(), view.getQuantitativeAnalysisTab().getQuantitativeAnalysisDTO()),
                    new FutureCallback<QuantitativeAnalysisResult>() {

                        @Override
                        public void onSuccess(QuantitativeAnalysisResult result) {
                            view.showQuantitativeAnalysisResult(result);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Notification.show("Error:" + throwable.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE);
                            log.warn("Item loading failed ", throwable);
                            view.getQuantitativeAnalysisTab().showLoading(false);
                        }
                    });
            executeDataSelection(view.getDataSelectionPanel().getData());
            view.getQuantitativeAnalysisTab().showLoading(true);
        } catch (FieldGroup.CommitException e) {
            log.debug("Validation failed", e);
        }
    }

    public void executeTimeSeriesCalculations() {

        try {
            if (view.getTimeSeriesTab().getTimeSeriesDTO().canExecuteCalculation()) {

                executeDataSelection(view.getDataSelectionPanel().getData());
                view.getTimeSeriesTab().showLoading(true);

                Futures.addCallback(ucTimeSeries.calculate(
                        view.getDataSelectionPanel().getData(),
                        view.getTimeSeriesTab().getTimeSeriesDTO()),
                        new FutureCallback<TimeSeriesResult>() {

                            @Override
                            public void onSuccess(TimeSeriesResult result) {
                                view.showTimeSeriesResults(result);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Notification.show("Error:" + throwable.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE);
                                log.warn("Item loading failed ", throwable);
                                view.getTimeSeriesTab().showLoading(false);
                            }
                        });
            }
        } catch (FieldGroup.CommitException e) {
            log.debug("Validation failed", e);
        }
    }

    private void executeDataSelection(DataSelectionDTO dto) {
        view.getDataSelectionPanel().showLoadingIndicator();

        Futures.addCallback(ucDataSelection.search(dto), new FutureCallback<Optional<DataSelectionResult>>() {
            @Override
            public void onSuccess(Optional<DataSelectionResult> result) {
                view.showSelectionDataResults(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Notification.show("Error:" + throwable.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE);
                log.warn("Item loading failed ", throwable);
            }
        });
    }

    public List<Integer> loadYears() {
        return ucLoadingYears.load();
    }

    public List<String> loadTitles() {
        return ucLoadingTitles.load();
    }

    public List<String> loadPeriods() {
        return ucLoadingPeriods.load();
    }

    public List<Integer> loadExpositions() {
        return ucLoadingExpositions.load();
    }

    public List<String> loadAuthors() {
        return ucLoadingAuthors.load();
    }
}
