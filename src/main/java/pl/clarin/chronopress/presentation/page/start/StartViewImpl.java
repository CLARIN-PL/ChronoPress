package pl.clarin.chronopress.presentation.page.start;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MGridLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.VaadinUI;
import pl.clarin.chronopress.presentation.page.concordance.ConcordanceView;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataSelectionForm;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.CalculationResult;
import pl.clarin.chronopress.presentation.page.frequency.FrequencyView;
import pl.clarin.chronopress.presentation.page.mapnames.MapnamesView;
import pl.clarin.chronopress.presentation.page.profile.ProfilesView;
import pl.clarin.chronopress.presentation.page.quantity.QuantityView;
import pl.clarin.chronopress.presentation.page.timeseries.TimeSeriesView;
import pl.clarin.chronopress.presentation.shered.dto.InitDataSelectionDTO;
import pl.clarin.chronopress.presentation.shered.event.NavigationEvent;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(StartView.ID)
public class StartViewImpl extends AbstractView<StartViewPresenter> implements StartView {

    @Inject
    private Instance<StartViewPresenter> presenter;

    @Inject
    javax.enterprise.event.Event<NavigationEvent> navigation;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    DataSelectionForm selectionForm;

    final TextField search = new TextField();

    private boolean filterVisible = false;

    private VerticalLayout content;

    CalculationResult calculation;

    private Panel linksPanel;

    private final Map<String, CalculationResult> results = new HashMap<>();

    @PostConstruct
    public void init() {
        search.addStyleName(ValoTheme.TEXTFIELD_LARGE);
        search.setWidth("60%");
        search.setImmediate(true);

        selectionForm.setVisible(false);

        String textInfo = "<span>Wpisz wyraz lub frazę i wyszukaj wystąpienia.</span></br>"
                + "<span>Na przykład:</span></br>"
                + "<span style=\"font-family: Courier;\">partia</span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp wyszukuje wystąpienia form <i>partia, partią, partiami itd.</i></br>"
                + "<span style=\"font-family: Courier;\">\"partią\"</span>&nbsp&nbsp&nbsp&nbsp wyszukuje wystąpienia dokładnie tego ciągu znaków.</br>"
                + "<span>Uwaga: system nie ropoznaje jako haseł podstawowych form nieciągłych typu <i>śmiać się</i>.</span></br>"
                + "<span>Wiecej opcji w oknie Konkordancje</span></br>";

        Label txt = new Label(VaadinUI.infoMessage(textInfo));
        Label opisTxt = new Label("Funkcjonalności portalu");
        opisTxt.addStyleName("press-text-align");

        HorizontalLayout opis = new MHorizontalLayout()
                .withWidth("60%")
                .with(opisTxt)
                .withAlign(opisTxt, Alignment.MIDDLE_CENTER);

        txt.setContentMode(ContentMode.HTML);

        VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(txt);

        // The component itself
        PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);
        PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        HorizontalLayout desc = new MHorizontalLayout()
                .with(new MHorizontalLayout(new MLabel("Wyszukaj w korpusie").withStyleName("press-text-large"), help2)
                        .withWidth("-1")
                        .withSpacing(true))
                .withWidth("60%");

        VerticalLayout searchContent = new MVerticalLayout()
                .with(desc, search, selectionForm, opis)
                .withAlign(desc, Alignment.MIDDLE_CENTER)
                .withAlign(search, Alignment.MIDDLE_CENTER)
                .withAlign(opis, Alignment.MIDDLE_CENTER)
                .withMargin(true)
                .withFullWidth();

        search.addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                getPresenter().onSearch(search.getValue());
            }
        });

        Button test1 = new MButton("Szeregi czasowe")
                .withSize(MSize.FULL_SIZE)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(TimeSeriesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test2 = new MButton("Profile wyrazów")
                .withSize(MSize.FULL_SIZE)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(ProfilesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test3 = new MButton("Konkordancje")
                .withSize(MSize.FULL_SIZE)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(ConcordanceView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test4 = new MButton("Mapa nazw miejscowych")
                .withSize(MSize.FULL_SIZE)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(MapnamesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test5 = new MButton("Listy frekwencyjne")
                .withSize(MSize.FULL_SIZE)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(FrequencyView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test6 = new MButton("Analiza ilościowa")
                .withSize(MSize.FULL_SIZE)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(QuantityView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        // frekwencja odmiany
        Button test7 = new MButton("Fleksja i frekwencja")
                .withSize(MSize.FULL_SIZE)
                .withListener(l -> {
                    Notification.show("Moduł w przygotowaniu", Notification.Type.HUMANIZED_MESSAGE);
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        GridLayout gridLayout = new MGridLayout()
                .withWidth(70, Unit.PERCENTAGE);

        gridLayout.setColumns(3);
        gridLayout.setRows(3);
        gridLayout.setColumnExpandRatio(0, 1);
        gridLayout.setColumnExpandRatio(1, 1);
        gridLayout.setColumnExpandRatio(2, 1);
        gridLayout.addComponent(test1, 0, 0);
        gridLayout.addComponent(test2, 1, 0);
        gridLayout.addComponent(test3, 2, 0);
        gridLayout.addComponent(test4, 0, 1);
        gridLayout.addComponent(test5, 1, 1);
        gridLayout.addComponent(test6, 2, 1);
        //gridLayout.addComponent(test7, 0, 2);

        Panel searchPanel = new MPanel(searchContent)
                .withStyleName(ValoTheme.PANEL_BORDERLESS)
                .withFullWidth();

        linksPanel = new MPanel(new MHorizontalLayout(gridLayout)
                .withAlign(gridLayout, Alignment.MIDDLE_CENTER)
                .withFullWidth()
        ).withFullWidth()
                .withStyleName(ValoTheme.PANEL_BORDERLESS);

        content = new MVerticalLayout(linksPanel)
                .withSize(MSize.FULL_SIZE);

        VerticalLayout layout = new MVerticalLayout()
                .with(searchPanel, content)
                .withMargin(true)
                .withFullHeight()
                .withFullWidth();

        setCompositionRoot(layout);
        setSizeFull();
    }

    @Override
    protected StartViewPresenter generatePresenter() {
        return presenter.get();
    }

    @Override
    public void addResultPanel(CalculationResult result) {
        getUI().access(() -> {
            setCalculation(result);
            show();
        });
    }

    public void show() {
        Component panel = calculation.showResult();
        content.removeAllComponents();
        content.addComponent(panel);
    }

    @Override
    public void setInitDataSelection(InitDataSelectionDTO data) {
        selectionForm.setAuthors(data.getAuthors());
        selectionForm.setYears(data.getYears());
        selectionForm.setExposition(data.getExpositions());
        selectionForm.setPeriods(data.getPeriods());
        selectionForm.setTiles(data.getJournalTitles());
        selectionForm.setAudience(data.getAudience());
    }

    public void setCalculation(CalculationResult calculation) {
        this.calculation = calculation;
        if (!results.containsKey(calculation.getType())) {
            results.put(calculation.getType(), calculation);
        }
    }

    public void onViewEnter() {
        content.removeAllComponents();
        content.addComponent(linksPanel);
    }
}
