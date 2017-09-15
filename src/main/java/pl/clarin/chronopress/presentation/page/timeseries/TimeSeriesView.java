package pl.clarin.chronopress.presentation.page.timeseries;

import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface TimeSeriesView extends ApplicationView<TimeSeriesViewPresenter> {

    public static final String ID = "szeregi-czasowe";

    public void addResultPanel(CalculationResult result);

    public void setInitDataSelection(InitDataSelectionDTO data);

    public void showConcordanceWindow(ConcordanceList list);
}
