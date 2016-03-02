package pl.edu.pwr.chrono.webui.ui.education;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.fields.MTable;
import pl.edu.pwr.chrono.domain.Page;
import pl.edu.pwr.chrono.domain.PageAggregator;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;


public class PageTab extends HorizontalLayout {

    private MTable pages;
    private DbPropertiesProvider provider;
    private Label content = new Label();

    public PageTab(PageAggregator aggregator, DbPropertiesProvider provider) {
        this.provider = provider;
        addStyleName(ChronoTheme.PAGE_TAB);
        setCaption(aggregator.getTitle());
        setMargin(new MarginInfo(true, true, true, false));
        setSpacing(true);
        setSizeFull();

        Panel panel = new Panel();
        panel.setSizeFull();
        panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        panel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

        content.setContentMode(ContentMode.HTML);
        content.setSizeFull();
        panel.setContent(content);

        initPageTable(aggregator);
        addComponent(pages);
        addComponent(panel);

        setExpandRatio(pages, 1);
        setExpandRatio(panel, 4);
    }

    private void initPageTable(PageAggregator aggregator) {
        pages = new MTable<>(Page.class)
                .withProperties("title")
                .withFullWidth();

        pages.addStyleName(ValoTheme.TABLE_COMPACT);
        pages.addStyleName(ValoTheme.TABLE_SMALL);
        pages.addStyleName(ValoTheme.TABLE_BORDERLESS);
        pages.addStyleName(ValoTheme.TABLE_NO_HEADER);
        pages.addStyleName(ValoTheme.TABLE_NO_STRIPES);
        pages.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        pages.addStyleName(ChronoTheme.PAGE_TABLE);
        pages.setSelectable(true);
        pages.addBeans(aggregator.getPages());

        if (aggregator.getPages().size() > 0) {
            pages.select(aggregator.getPages().get(0));
            content.setValue(aggregator.getPages().get(0).getContent());
        }

        pages.addRowClickListener(event -> {
            content.setValue(((Page) event.getEntity()).getContent());
        });
    }

}
