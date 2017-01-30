package pl.clarin.chronopress.presentation.page.start;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MGridLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.concordance.ConcordanceView;
import pl.clarin.chronopress.presentation.page.dataanalyse.DataSelectionForm;
import pl.clarin.chronopress.presentation.page.frequency.FrequencyView;
import pl.clarin.chronopress.presentation.page.mapnames.MapnamesView;
import pl.clarin.chronopress.presentation.page.profile.ProfilesView;
import pl.clarin.chronopress.presentation.page.quantity.QuantityView;
import pl.clarin.chronopress.presentation.page.timeseries.TimeSeriesView;
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

    @PostConstruct
    public void init() {
        search.addStyleName(ValoTheme.TEXTFIELD_LARGE);
        search.setWidth("70%");
        search.setImmediate(true);

        selectionForm.setVisible(false);

        Label txt = new Label("<p>Wpisz wyraz lub frazę i wyszukaj wystąpienia.</p></br>"
                + "<p>Na przykład:</p></br>"
                + "<div style=\"font: Curier;\">partia</div>&nbsp&nbsp&nbsp&nbsp wyszukuje wystąpienia form <i>partia, partią, partiami itd.</i>"
                + "<div style=\"font: Curier;\">\"z partią\"</div>&nbsp&nbsp&nbsp&nbsp wyszukuje wystąpienia dokładnie tego ciągu znaków."
                + "<p>Uwaga: system nie ropoznaje jako haseł podstawowych form nieciągłych typu <i>śmiać się</i>.</p></br>"
                + "<p>Wiecej opcji w oknie Konkordancje</p></br>");

        Label opis = new Label("Funkcjonalności portalu");

        txt.setContentMode(ContentMode.HTML);

        VerticalLayout popupContent = new VerticalLayout();
        popupContent.addComponent(txt);

        // The component itself
        PopupView help = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);
        PopupView help2 = new PopupView(FontAwesome.QUESTION_CIRCLE.getHtml(), popupContent);

        HorizontalLayout desc = new MHorizontalLayout(new Label("Wyszukaj w korpusie"), help2);

        VerticalLayout searchContent = new MVerticalLayout()
                .with(desc, search, selectionForm, opis)
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
                .withIcon(FontAwesome.ANDROID)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(TimeSeriesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test2 = new MButton("Profile wyrazów")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.GIT_SQUARE)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(ProfilesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test3 = new MButton("Konkordancje")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.BOLT)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(ConcordanceView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test4 = new MButton("Mapa nazw miejscowych")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.COMMENTING)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(MapnamesView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test5 = new MButton("Listy frekwencyjne")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.INBOX)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(FrequencyView.ID));
                })
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test6 = new MButton("Analiza ilościowa")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.SCRIBD)
                .withListener(l -> {
                    navigation.fire(new NavigationEvent(QuantityView.ID));
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

        Panel searchPanel = new MPanel(searchContent)
                .withStyleName(ValoTheme.PANEL_BORDERLESS)
                .withFullWidth();

        Panel linksPanel = new MPanel(new MHorizontalLayout(gridLayout)
                .withAlign(gridLayout, Alignment.MIDDLE_CENTER)
                .withFullWidth()
        ).withFullWidth()
                .withStyleName(ValoTheme.PANEL_BORDERLESS);

        VerticalLayout content = new MVerticalLayout()
                .with(searchPanel, linksPanel)
                //.withStyleName(ChronoTheme.START_PANEL)
                .withMargin(true)
                .withFullHeight()
                .withFullWidth();

        setCompositionRoot(content);
        setSizeFull();
    }

    @Override
    protected StartViewPresenter generatePresenter() {
        return presenter.get();
    }
}
