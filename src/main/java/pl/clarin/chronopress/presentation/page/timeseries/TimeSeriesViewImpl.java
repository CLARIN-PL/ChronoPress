package pl.clarin.chronopress.presentation.page.timeseries;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.dataanalyse.CalculateTimeSerieEvent;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataSelectionForm;
import pl.clarin.chronopress.presentation.page.dataanalyse.TimeSeriesForm;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.shered.dto.TimeSeriesDTO;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(TimeSeriesView.ID)
public class TimeSeriesViewImpl extends AbstractView<TimeSeriesViewPresenter> implements TimeSeriesView {

    @Inject
    private Instance<TimeSeriesViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    DataSelectionForm selectionForm;

    @Inject
    TimeSeriesForm timeSeriesForm;
    
    @Inject
    javax.enterprise.event.Event<CalculateTimeSerieEvent> calculateTimeSeries;

    private VerticalLayout loading;

    private HorizontalLayout buttonWrapper;

    private final Map<String, CalculationResult> results = new HashMap<>();

    CalculationResult calculation;

    private boolean filterVisible = false;

    private VerticalLayout layout;

    public TimeSeriesDTO getTimeSeriesDTO() {
        try {
            return timeSeriesForm.getTimeSeriesDTO();
        } catch (FieldGroup.CommitException ex) {
            Logger.getLogger(TimeSeriesViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void reset() {
        timeSeriesForm.reset();
    }

    @PostConstruct
    public void init() {
        selectionForm.setVisible(false);

        Label desc = new Label("Szeregi czasowe .....");
        Button filter = new MButton("Ustawienia filtra")
                .withStyleName(ValoTheme.BUTTON_TINY, ValoTheme.BUTTON_LINK)
                .withListener(l -> {
                    filterVisible = !filterVisible;
                    selectionForm.setVisible(filterVisible);
                });

        Button execute = new MButton("Wyszukaj szeregów")
                .withListener(l -> {

                    calculateTimeSeries.fire(new CalculateTimeSerieEvent(selectionForm.getData(), getTimeSeriesDTO()));

                })
                .withStyleName(ValoTheme.BUTTON_SMALL);

        VerticalLayout content = new MVerticalLayout()
                .with(desc, filter, selectionForm, timeSeriesForm, execute)
                .withStyleName(ChronoTheme.START_PANEL)
                .withMargin(true)
                .withFullHeight()
                .withFullWidth();

        layout = new MVerticalLayout()
                .withSpacing(true)
                .withMargin(false)
                .withFullWidth()
                .with(content);

        setCompositionRoot(layout);
        setSizeFull();
    }

    private VerticalLayout initializeLoading() {

        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);

        HorizontalLayout loadingNotification = new MHorizontalLayout()
                .withSpacing(true)
                .with(progressBar, new Label(provider.getProperty("label.loading")));

        return new MVerticalLayout()
                .withMargin(true)
                .with(loadingNotification)
                .withAlign(loadingNotification, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void addResultPanel(CalculationResult result) {
        getUI().access(() -> {
            setCalculation(result);
            show();
        });
    }

    public void show() {
        Component panel = createContentWrapper(calculation.showResult(), calculation.getType());
        layout.addComponent(panel);
    }

    public void setCalculation(CalculationResult calculation) {
        this.calculation = calculation;
        if (!results.containsKey(calculation.getType())) {
            results.put(calculation.getType(), calculation);
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

    @Override
    protected TimeSeriesViewPresenter generatePresenter() {
        return presenter.get();
    }

    private Component createContentWrapper(final Component content, String type) {

        content.setCaption(null);
        content.addStyleName(ChronoTheme.PANEL_CONTENT);
        Label caption = new MLabel(content.getCaption())
                .withStyleName(ValoTheme.LABEL_H4)
                .withStyleName(ValoTheme.LABEL_COLORED)
                .withStyleName(ValoTheme.LABEL_NO_MARGIN);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

        HorizontalLayout toolbar = new MHorizontalLayout()
                .withStyleName(ChronoTheme.PANEL_TOOLBAR)
                .withFullWidth()
                .with(caption, tools)
                .withExpand(caption, 1)
                .withAlign(caption, Alignment.MIDDLE_LEFT);

        CssLayout card = new MCssLayout(toolbar, content)
                .withFullWidth()
                .withStyleName(ValoTheme.LAYOUT_CARD);

        final CssLayout slot = new MCssLayout(card)
                .withFullWidth();
        slot.setId(type);

        MenuBar.MenuItem root = tools.addItem("", FontAwesome.TIMES_CIRCLE, (MenuBar.Command) selectedItem -> {
            results.remove(slot.getId());
            layout.removeComponent(slot);
        });
        return slot;
    }
}
