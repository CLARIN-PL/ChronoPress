package pl.edu.pwr.chrono.webui.ui.admin.education;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;
import pl.edu.pwr.chrono.domain.Page;
import pl.edu.pwr.chrono.domain.PageAggregator;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView(name = EducationEditorView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class EducationEditorView extends DefaultView<EducationEditorPresenter> implements View {

    public static final String VIEW_NAME = "education-editor";

    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    private PageWindow pageWindow;

    @Autowired
    private CategoryWindow categoryWindow;

    private MTable aggregations;
    private MTable pages;

    @Autowired
    public EducationEditorView(EducationEditorPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(provider.getProperty("view.admin.education.editor.title")));
        setSpacing(true);

        initAggregatePageTable();
        initPageTable();

        HorizontalLayout buttons = buildButtons();

        pageWindow.getCancel().addClickListener(event -> {
            UI.getCurrent().removeWindow(pageWindow);
        });

        categoryWindow.getCancel().addClickListener(event -> {
            UI.getCurrent().removeWindow(categoryWindow);
        });

        pageWindow.getSave().addClickListener(event -> {
            presenter.savePage(pageWindow.getItem());
            loadAggregator();
            UI.getCurrent().removeWindow(pageWindow);
        });

        pageWindow.getDelete().addClickListener(event -> {
            presenter.removePage(pageWindow.getItem());
            loadAggregator();
            UI.getCurrent().removeWindow(pageWindow);
        });


        categoryWindow.getSave().addClickListener(event -> {
            presenter.savePageAggregator(categoryWindow.getItem());
            loadAggregator();
            UI.getCurrent().removeWindow(categoryWindow);
        });

        HorizontalSplitPanel panel = new HorizontalSplitPanel();
        panel.setSplitPosition(30, Unit.PERCENTAGE);
        panel.addComponent(aggregations);
        panel.addComponent(pages);

        addComponent(buttons);
        addComponent(panel);
    }

    private HorizontalLayout buildButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button createCategory = new Button(provider.getProperty("button.create.category"));
        createCategory.addStyleName(ValoTheme.BUTTON_TINY);
        createCategory.addStyleName(ChronoTheme.BUTTON);
        createCategory.setIcon(FontAwesome.PLUS_CIRCLE);
        createCategory.addClickListener(event -> {
            PageAggregator p = new PageAggregator();
            categoryWindow.setItem(p);
            UI.getCurrent().addWindow(categoryWindow);
        });

        Button createPage = new Button(provider.getProperty("button.create.page"));
        createPage.addStyleName(ValoTheme.BUTTON_TINY);
        createPage.addStyleName(ChronoTheme.BUTTON);
        createPage.setIcon(FontAwesome.PLUS_SQUARE);
        createPage.addClickListener(event -> {
            Page p = new Page();
            p.setPublished(false);
            p.setPageAggregator((PageAggregator) aggregations.getValue());
            pageWindow.setItem(p);
            UI.getCurrent().addWindow(pageWindow);
        });

        buttons.addComponent(createCategory);
        buttons.addComponent(createPage);
        return buttons;
    }

    private void initPageTable() {
        pages = new MTable<>(Page.class)
                .withProperties("title", "published")
                .withColumnHeaders(
                        provider.getProperty("label.page.title"),
                        provider.getProperty("label.page.published"))
                .withFullHeight().withFullWidth();
        pages.addStyleName(ValoTheme.TABLE_COMPACT);
        pages.addStyleName(ValoTheme.TABLE_SMALL);
        pages.setSelectable(true);
        pages.addRowClickListener(event -> {
            pageWindow.setItem((Page) event.getEntity());
            UI.getCurrent().addWindow(pageWindow);
        });
    }

    private void initAggregatePageTable() {
        aggregations = new MTable<>(PageAggregator.class)
                .withProperties("title")
                .withColumnHeaders(
                        provider.getProperty("label.page.category"))
                .withFullWidth();

        aggregations.addStyleName(ValoTheme.TABLE_COMPACT);
        aggregations.addStyleName(ValoTheme.TABLE_SMALL);
        aggregations.setSelectable(true);
        aggregations.addRowClickListener(event -> {
            pages.removeAllItems();
            pages.addBeans(((PageAggregator) event.getEntity()).getPages());
        });
    }

    private void loadAggregator() {
        List<PageAggregator> list = presenter.getPageAggregators();
        aggregations.removeAllItems();
        aggregations.addBeans(list);
        if (list.size() > 0) {
            aggregations.select(list.get(0));
            pages.removeAllItems();
            pages.addBeans(list.get(0).getPages());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        loadAggregator();
    }
}
