package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.cdi.CDIView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.page.samplebrowser.SampleWindow;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(DataAnalyseView.ID)
public class DataAnalyseViewImpl extends AbstractView<DataAnalyseViewPresenter> implements DataAnalyseView {

    @Inject
    private Instance<DataAnalyseViewPresenter> presenterInstance;

    @Inject
    DataAnalyseScreen dataAnalyseScreen;

    @Inject
    ConcordanceWindow concordanceWindow;

    @Inject
    SampleWindow sampleWindow;

    private VerticalLayout layout;

    @PostConstruct
    public void init() {

        layout = new MVerticalLayout()
                .withSize(MSize.FULL_SIZE)
                .withMargin(new MMarginInfo(false, true, true, true));

        setCompositionRoot(layout);
    }

    @Override
    public void showDataAnalyseScreen() {
        getUI().access(() -> {
            layout.removeAllComponents();
            layout.addComponent(dataAnalyseScreen);
        });
    }

    @Override
    public void addResultPanel(CalculationResult result) {
        getUI().access(() -> {
            dataAnalyseScreen.hideLoadingIndicator();
            dataAnalyseScreen.setCalculation(result);
            dataAnalyseScreen.show();
        });
    }

    @Override
    public void showLoading() {
        getUI().access(() -> {
            dataAnalyseScreen.showLoadingIndicator();
        });
    }

    @Override
    protected DataAnalyseViewPresenter generatePresenter() {
        return presenterInstance.get();
    }

    public void initDataAnalyseScreen(InitDataSelectionDTO data) {
        dataAnalyseScreen.setInitDataSelection(data);
    }

    @Override
    public void showConcordanceWindow(ConcordanceList list) {
        concordanceWindow.setConcordance(list);
        UI.getCurrent().addWindow(concordanceWindow);
    }

    @Override
    public void showSampleWindow(Sample sample, String lemma) {
        sampleWindow.setItemWithMarkedkWord(sample, lemma);
        UI.getCurrent().addWindow(sampleWindow);
    }

    @Override
    public void hideLoading() {
        dataAnalyseScreen.hideLoadingIndicator();
    }

    @Override
    public void selectConcordance(String lemma) {
        dataAnalyseScreen.selectConcordance(lemma);
    }
}
