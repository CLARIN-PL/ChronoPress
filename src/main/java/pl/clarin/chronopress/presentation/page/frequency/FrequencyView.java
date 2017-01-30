package pl.clarin.chronopress.presentation.page.frequency;

import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface FrequencyView extends ApplicationView<FrequencyViewPresenter> {

    public static final String ID = "frekwencje";

    public void addResultPanel(CalculationResult result);

    public void setInitDataSelection(InitDataSelectionDTO data);
}
