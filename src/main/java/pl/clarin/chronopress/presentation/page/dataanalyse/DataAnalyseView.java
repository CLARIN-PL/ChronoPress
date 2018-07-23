package pl.clarin.chronopress.presentation.page.dataanalyse;

import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.ApplicationView;

/**
 *
 * @author tnaskret
 */
public interface DataAnalyseView extends ApplicationView<DataAnalyseViewPresenter> {

    public static final String ID = "data-analyse";

    void addResultPanel(CalculationResult result);

    void initDataAnalyseScreen(InitDataSelectionDTO data);

    void showDataAnalyseScreen();

    void showConcordanceWindow(ConcordanceList list);

    void showSampleWindow(Sample sample, String lemma);

    void showLoading();

    void hideLoading();

    void selectConcordance(String lemma);
}
