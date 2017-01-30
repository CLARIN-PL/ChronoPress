package pl.clarin.chronopress.presentation.page.mapnames;

import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface MapnamesView extends ApplicationView<MapnamesViewPresenter> {

    public static final String ID = "nazwy-miejscowe";

    public void addResultPanel(CalculationResult result);

    public void setInitDataSelection(InitDataSelectionDTO data);
}
