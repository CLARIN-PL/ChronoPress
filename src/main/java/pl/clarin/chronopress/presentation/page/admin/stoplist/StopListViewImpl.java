package pl.clarin.chronopress.presentation.page.admin.stoplist;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.stoplist.entity.StopList;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(StopListView.ID)
@RolesAllowed({"moderator"})
public class StopListViewImpl extends AbstractView<StopListViewPresenter> implements StopListView {

    @Inject
    private Instance<StopListViewPresenter> presenterInstance;

    private final Grid grid = new Grid();

    @Inject
    DbPropertiesProvider provider;
    private BeanItemContainer<StopList> container = new BeanItemContainer<>(StopList.class);

    @PostConstruct
    public void init() {

        MVerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .with(new Title(provider.getProperty("view.admin.stop.list.title")),
                        buildAddNameSection(), grid);
        iniGridColumns();
        
        setCompositionRoot(layout);
    }

    public HorizontalLayout buildAddNameSection() {

        MTextField name = new MTextField()
                .withStyleName(ValoTheme.TEXTAREA_TINY);

        MButton addName = new MButton()
                .withCaption(provider.getProperty("label.add.stop.list.item.name"))
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withIcon(FontAwesome.PLUS_CIRCLE)
                .withListener(event -> {
                    StopList item = new StopList();
                    item.setName(name.getValue());
                    container.addBean(getPresenter().save(item));
                });

        MButton removeSelected = new MButton()
                .withCaption(provider.getProperty("label.remove.stop.list.item.name"))
                .withIcon(FontAwesome.TRASH_O)
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withListener(event -> {
                    if (grid.getSelectedRows().size() > 0) {
                        grid.getSelectedRows().forEach(o -> {
                            getPresenter().delete(((StopList)o).getId());
                            container.removeItem(o);
                        });
                    }
                });

        return new MHorizontalLayout()
                .withSpacing(true)
                .with(new Label(provider.getProperty("label.name")),
                        name, addName, removeSelected);
    }

    private void iniGridColumns() {
        grid.setContainerDataSource(container);
        grid.getColumn("id").setHidden(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(15);
    }
    
    @Override
    public void setStopList(List<StopList> list) {
        container.removeAllItems();
        container.addAll(list);
    }

    @Override
    protected StopListViewPresenter generatePresenter() {
        return presenterInstance.get();
    }
}
