package pl.clarin.chronopress.presentation.page.start;

import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface StartView extends ApplicationView<StartViewPresenter> {

    public static final String ID = "start";

    void addResultPanel(CalculationResult result);

    void setInitDataSelection(InitDataSelectionDTO data);

    void onViewEnter();

}
