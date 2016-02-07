package pl.edu.pwr.chrono.webui.ui.dataanalyse;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.readmodel.*;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.webui.infrastructure.Presenter;

import java.util.List;
import java.util.Set;

/**
 * Created by tnaskret on 10.01.16.
 */
@SpringComponent
@UIScope
public class DataAnalysePresenter extends Presenter<DataAnalyseView> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataAnalysePresenter.class);

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

    public void acceptDataSelection(){
        DataSelectionDTO dto = new DataSelectionDTO();

        Set<Integer> selectedYears = (Set<Integer>) view.getDataSelectionTab().getYears().getValue();
        Set<String> selectedTitles = (Set<String>) view.getDataSelectionTab().getTitles().getValue();
        Set<String> selectedPeriods = (Set<String>) view.getDataSelectionTab().getPeriods().getValue();
        Set<Integer> selectedExpositions = (Set<Integer>) view.getDataSelectionTab().getExpositions().getValue();

        dto.setYears(selectedYears);
        dto.setTitles(selectedTitles);
        dto.setPeriodicType(selectedPeriods);
        dto.setExposition(selectedExpositions);

        DataSelectionResult result =  ucDataSelection.search(dto);

        view.getDataSelectionTab().showResults(result.getSampleCount(),result.getWordCount());
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
}
