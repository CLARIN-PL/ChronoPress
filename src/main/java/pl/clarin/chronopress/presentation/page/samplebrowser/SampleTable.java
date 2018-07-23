package pl.clarin.chronopress.presentation.page.samplebrowser;

import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.MTable;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.sample.entity.Sample;

public class SampleTable extends CustomComponent {

    private MTable<Sample> table;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    javax.enterprise.event.Event<ShowSampleEvent> showWindow;

    @PostConstruct
    public void initialize() {

        table = new MTable<>(Sample.class)
                .withProperties("journalTitle", "articleTitle", "authors", "style", "date")
                .withColumnHeaders(
                        provider.getProperty("label.journal.title"),
                        provider.getProperty("label.article.title"),
                        provider.getProperty("label.authors"),
                        provider.getProperty("label.style"),
                        provider.getProperty("label.published.date"))
                .withSize(MSize.FULL_SIZE)
                .withColumnExpand("articleTitle", 1);
        table.setSortableProperties("journalTitle", "articleTitle", "date", "authors");
        table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        table.setSelectable(true);
        table.setPageLength(16);
        table.setConverter("date", new StringToDateConverter() {
            @Override
            public DateFormat getFormat(Locale locale) {

                return new SimpleDateFormat("dd-MM-yyyy");
            }
        });

        table.addRowClickListener(event -> {
            if (event.isDoubleClick()) {
                showWindow.fire(new ShowSampleEvent(event.getEntity()));
            }
        });

        setCompositionRoot(table);
    }

    public void setBeans(SortableLazyList list) {
        table.setBeans(list);
    }
}
