package pl.clarin.chronopress.presentation.page.start;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
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
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(StartView.ID)
public class StartViewImpl extends AbstractView<StartViewPresenter> implements StartView {

    @Inject
    private Instance<StartViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    final TextField search = new TextField("Wyszukaj słowa");

    @PostConstruct
    public void init() {
        search.addStyleName(ValoTheme.TEXTFIELD_LARGE);
        search.setWidth("70%");
        search.setImmediate(true);

        VerticalLayout searchContent = new MVerticalLayout()
                .with(search)
                .withAlign(search, Alignment.MIDDLE_CENTER)
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
                .withIcon(FontAwesome.BELL)
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test2 = new MButton("Profile")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.GIT_SQUARE)
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test3 = new MButton("Konkordancje")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.BOLT)
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test4 = new MButton("Mapa nazw miejscowych")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.COMMENTING)
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test5 = new MButton("Listy frekwencyjne")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.INBOX)
                .withStyleName(ValoTheme.BUTTON_LARGE);

        Button test6 = new MButton("Analiza ilościowa")
                .withSize(MSize.FULL_SIZE)
                .withIcon(FontAwesome.SCRIBD)
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
    public void enter(ViewChangeEvent event) {
    }

    @Override
    protected StartViewPresenter generatePresenter() {
        return presenter.get();
    }
}
