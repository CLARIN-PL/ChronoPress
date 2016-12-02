package pl.clarin.chronopress.presentation.page.admin;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MGridLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.about.AboutView;
import pl.clarin.chronopress.presentation.page.admin.audience.AudienceView;
import pl.clarin.chronopress.presentation.page.admin.datamanagment.DataManagementView;
import pl.clarin.chronopress.presentation.page.admin.education.EducationManagmenView;
import pl.clarin.chronopress.presentation.page.admin.labels.LabelsView;
import pl.clarin.chronopress.presentation.page.admin.lexicalfield.LexicalFieldView;
import pl.clarin.chronopress.presentation.page.admin.propernames.ProperNameView;
import pl.clarin.chronopress.presentation.page.admin.stoplist.StopListView;
import pl.clarin.chronopress.presentation.page.admin.users.UsersView;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(AdminView.ID)
@RolesAllowed({"moderator"})
public class AdminViewImpl extends AbstractView<AdminViewPresenter> implements AdminView {

    @Inject
    Instance<AdminViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    @PostConstruct
    public void init() {

        MGridLayout gridLayout = new MGridLayout()
                .withWidth(70, Unit.PERCENTAGE);
        gridLayout.setColumns(3);
        gridLayout.setRows(3);
        gridLayout.setColumnExpandRatio(0, 1);
        gridLayout.setColumnExpandRatio(1, 1);
        gridLayout.setColumnExpandRatio(2, 1);
        gridLayout.addComponent(addNavigetionButton("view.admin.panel.lexical.field.button", FontAwesome.LEAF, LexicalFieldView.ID), 0, 0);
        gridLayout.addComponent(addNavigetionButton("view.admin.panel.audience.button", FontAwesome.USERS, AudienceView.ID), 1, 0);
        gridLayout.addComponent(addNavigetionButton("view.admin.panel.education.button", FontAwesome.GRADUATION_CAP, EducationManagmenView.ID), 2, 0);
        gridLayout.addComponent(addNavigetionButton("view.admin.panel.caption.button", FontAwesome.TAGS, LabelsView.ID), 0, 1);
        gridLayout.addComponent(addNavigetionButton("view.admin.panel.data.button", FontAwesome.DATABASE, DataManagementView.ID), 1, 1);
        gridLayout.addComponent(addNavigetionButton("view.admin.panel.user.button", FontAwesome.USER, UsersView.ID), 2, 1);
        gridLayout.addComponent(addNavigetionButton("view.admin.stop.list.title", FontAwesome.STOP, StopListView.ID), 0, 2);
        gridLayout.addComponent(addNavigetionButton("view.admin.panel.home.page.button", FontAwesome.HOME, AboutView.ID), 1, 2);
        gridLayout.addComponent(addNavigetionButton("view.admin.panel.propername.button", FontAwesome.MAP, ProperNameView.ID), 2, 2);

        MHorizontalLayout wrapper = new MHorizontalLayout()
                .withWidth(100, Unit.PERCENTAGE)
                .with(gridLayout)
                .withAlign(gridLayout, Alignment.MIDDLE_CENTER);

        VerticalLayout content = new MVerticalLayout()
                .withMargin(new MarginInfo(false, true, true, true))
                .with(new Title(provider.getProperty("view.admin.panel.title")), wrapper);

        setCompositionRoot(content);
    }

    private MButton addNavigetionButton(String name, FontAwesome icon, String view) {
        MButton button = new MButton(provider.getProperty(name))
                .withIcon(icon)
                .withStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP)
                .withWidth(100, Unit.PERCENTAGE);

        if (AboutView.ID.equals(view)) {
            button.addClickListener(() -> getPresenter().showEditWindow());
        } else {
            button.addClickListener(() -> getPresenter().navigateTo(view));
        }
        return button;
    }

    @Override
    protected AdminViewPresenter generatePresenter() {
        return presenter.get();
    }

}
