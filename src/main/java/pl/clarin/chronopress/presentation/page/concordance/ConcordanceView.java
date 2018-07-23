package pl.clarin.chronopress.presentation.page.concordance;

import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface ConcordanceView extends ApplicationView<ConcordanceViewPresenter> {

    public static final String ID = "konkordancje";

    public void addResultPanel(CalculationResult result);

    public void setInitDataSelection(InitDataSelectionDTO data);

    public void showConcordanceWindow(ConcordanceList list);

    public void showSampleWindow(Sample sample, String lemma);

}
