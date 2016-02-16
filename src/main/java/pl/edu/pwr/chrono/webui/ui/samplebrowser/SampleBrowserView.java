package pl.edu.pwr.chrono.webui.ui.samplebrowser;

import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.MTable;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.repository.TextRepository;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@SpringView(name = SampleBrowserView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class SampleBrowserView extends DefaultView<SampleBrowserPresenter> implements View {

    public static final String VIEW_NAME = "sample-browser";

    private MTable<Text> table;


    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    private TextWindow window;

    @Autowired
    public SampleBrowserView(SampleBrowserPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(FontAwesome.BOOK, provider.getProperty("view.sample.browser.title")));
        setSpacing(true);
        setWidth(100, Unit.PERCENTAGE);

        initializeTable();
        addComponent(table);

    }

    private void initializeTable() {
        table = new MTable<>(Text.class)
                .withProperties("journalTitle", "articleTitle", "authors", "style", "date")
                .withColumnHeaders(
                        provider.getProperty("label.journal.title"),
                        provider.getProperty("label.article.title"),
                        provider.getProperty("label.authors"),
                        provider.getProperty("label.style"),
                        provider.getProperty("label.published.date"))
                .setSortableProperties("journalTitle", "articleTitle", "date", "authors").withFullWidth();

        table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        table.setSelectable(true);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        table.addStyleName(ValoTheme.TABLE_SMALL);
        table.setPageLength(20);
        table.setBeans(new SortableLazyList<>(
                // entity fetching strategy
                (firstRow, asc, sortProperty) -> presenter.findEntities(firstRow, asc, sortProperty),
                // count fetching strategy
                () -> (int) presenter.size(), TextRepository.PAGE_SIZE));

        table.setConverter("date", new StringToDateConverter() {
            @Override
            public DateFormat getFormat(Locale locale) {

                return new SimpleDateFormat("dd-MM-yyyy");
            }
        });
        
        table.addRowClickListener(event -> {
            window.setItem(event.getEntity());
            MainUI.getCurrent().addWindow(window);
        });
    }


    @Override
    public void enter(ViewChangeEvent event) {
    }
}