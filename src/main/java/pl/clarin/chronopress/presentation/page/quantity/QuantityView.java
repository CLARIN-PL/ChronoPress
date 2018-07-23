package pl.clarin.chronopress.presentation.page.quantity;

import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface QuantityView extends ApplicationView<QuantityViewPresenter> {

    public static final String ID = "analiza-ilosciowa";

    public void addResultPanel(CalculationResult result);

    public void setInitDataSelection(InitDataSelectionDTO data);
}
