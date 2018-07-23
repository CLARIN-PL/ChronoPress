package pl.clarin.chronopress.presentation.page.admin.audience;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.audience.entity.Audience;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.layout.MComboBox;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(AudienceView.ID)
@RolesAllowed({"moderator"})
public class AudienceViewImpl extends AbstractView<AudienceViewPresenter> implements AudienceView {

    @Inject
    private Instance<AudienceViewPresenter> presenterInstance;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    AudienceWindow window;

    @Inject
    NamesTable namesTable;

    @Inject
    javax.enterprise.event.Event<AudienceDeleteEvent> deleteEvent;

    private final MComboBox<Audience> audience = new MComboBox<>("audienceName", Audience.class);

    @PostConstruct
    public void init() {

        audience.addStyleName(ValoTheme.COMBOBOX_TINY);
        audience.addValueChangeListener(e -> {
            if (audience.getBean() != null) {
                namesTable.addBeans(audience.getBean().getJournalTitle());
            }
        });

        MVerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .with(new Title(provider.getProperty("view.admin.audience.title")),
                        buildAudienceSelection(),
                        namesTable);
        setCompositionRoot(layout);
    }

    public HorizontalLayout buildAudienceSelection() {

        Button createAudience = new MButton(provider.getProperty("button.create.audience.group"))
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withIcon(FontAwesome.PLUS_CIRCLE)
                .withListener(e -> {
                    window.setAudience(new Audience());
                    window.setReferenceNames(presenterInstance.get().getJournalTitles());
                    UI.getCurrent().addWindow(window);
                });

        Button delete = new MButton(provider.getProperty("button.delete.audience.group"))
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withIcon(FontAwesome.TRASH_O)
                .withListener(e -> {
                    Audience item = audience.getBean();
                    if (item != null) {
                        audience.removeBean(item);
                        namesTable.removeAll();
                        deleteEvent.fire(new AudienceDeleteEvent(item));
                    }
                });

        Button edit = new MButton(provider.getProperty("button.edit.audience.group"))
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withIcon(FontAwesome.EDIT)
                .withListener(e -> {
                    if (audience.getBean() != null) {
                        window.setAudience(audience.getBean());
                        window.setReferenceNames(presenterInstance.get().getJournalTitles());
                        UI.getCurrent().addWindow(window);
                    }
                });

        HorizontalLayout layout = new MHorizontalLayout()
                .withSpacing(true)
                .with(new Label(provider.getProperty("label.audience.group")),
                        audience, edit, createAudience, delete);

        return layout;
    }

    @Override
    public void setAudience(List<Audience> list) {
        audience.addBeans(list);
    }

    @Override
    protected AudienceViewPresenter generatePresenter() {
        return presenterInstance.get();
    }

    @Override
    public void swapAudience(Audience old, Audience actual) {
        audience.removeBean(old);
        audience.addBean(actual);
    }
}
