package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.*;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by tnaskret on 10.01.16.
 */
@SpringComponent
@UIScope
public class DataAnalysisPresenter extends Presenter<DataAnalysisView> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataAnalysisPresenter.class);

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
    private  UCQuantitativeAnalysis ucQuantitativeAnalysis;

    private DataSelectionResult dataSelectionResult;

    public void onAcceptDataSelection(){
        DataSelectionDTO dto = new DataSelectionDTO();

        Set<Integer> selectedYears = (Set<Integer>) view.getDataSelectionTab().getYears().getValue();
        Set<String> selectedTitles = (Set<String>) view.getDataSelectionTab().getTitles().getValue();
        Set<String> selectedPeriods = (Set<String>) view.getDataSelectionTab().getPeriods().getValue();
        Set<Integer> selectedExpositions = (Set<Integer>) view.getDataSelectionTab().getExpositions().getValue();
        List<String> selectedAuthors =  view.getDataSelectionTab().getSearchAuthorsPanel().getSelectedItems();

        dto.setYears(selectedYears);
        dto.setTitles(selectedTitles);
        dto.setPeriodicType(selectedPeriods);
        dto.setExposition(selectedExpositions);
        dto.setAuthors(selectedAuthors);

        executeDataSelection(dto);
    }

    public void onQuantitativeAnalysis(){
       QuantitativeAnalysisResult result = ucQuantitativeAnalysis.calculate(
                getDataSelectionResult(), view.getQuantitativeAnalysisTab().getQuantitativeAnalysisDTO());
       view.getQuantitativeAnalysisTab().showChart(result);
    }

    private void setDataSelectionResult(DataSelectionResult result){
        this.dataSelectionResult = result;
    }

    private DataSelectionResult getDataSelectionResult(){
        return dataSelectionResult;
    }

    private void executeDataSelection(DataSelectionDTO dto) {
        view.getDataSelectionTab().showLoadingIndicator();

        Futures.addCallback(ucDataSelection.search(dto), new FutureCallback<Optional<DataSelectionResult>>() {
            @Override
            public void onSuccess(Optional<DataSelectionResult> result) {
                setDataSelectionResult(result.get());
                view.showSelectionDataResults(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.warn("Item loading failed ", throwable);
            }
        });
    }

    public List<Integer> loadYears(){
        return  ucLoadingYears.load();
    }

    public List<String> loadTitles(){
        return ucLoadingTitles.load();
    }

    public List<String> loadPeriods(){
        return  ucLoadingPeriods.load();
    }

    public  List<Integer> loadExpositions(){
        return  ucLoadingExpositions.load();
    }

    public List<String> loadAuthors(){
        return  ucLoadingAuthors.load();
    }
}
