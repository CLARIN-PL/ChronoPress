package pl.clarin.chronopress.presentation.page.education;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import org.vaadin.viritin.fields.MTable;
import pl.clarin.chronopress.business.education.entity.TitleDTO;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class PageTitlesTable extends CustomComponent {

    private MTable<TitleDTO> categories;

    @PostConstruct
    public void init() {
        categories = new MTable<>(TitleDTO.class)
                .withProperties("title")
                .withFullWidth()
                .withFullHeight();

        categories.addStyleName(ValoTheme.TABLE_COMPACT);
        categories.addStyleName(ValoTheme.TABLE_SMALL);
        categories.addStyleName(ValoTheme.TABLE_BORDERLESS);
        categories.addStyleName(ValoTheme.TABLE_NO_HEADER);
        categories.addStyleName(ValoTheme.TABLE_NO_STRIPES);
        categories.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        categories.addStyleName(ChronoTheme.PAGE_TABLE);
        categories.setSelectable(true);

        setCompositionRoot(categories);
    }

    public void setRowClickListener(MTable.RowClickListener<TitleDTO> listener) {
        categories.addRowClickListener(listener);
    }

    public void setCategories(List<TitleDTO> list) {
        categories.addBeans(list);
        sort();
    }

    public void select(TitleDTO item) {
        categories.select(item);
    }

    public void sort() throws UnsupportedOperationException {
        Object[] properties = {"title"};
        boolean[] ordering = {true};
        categories.sort(properties, ordering);
    }
}
