package pl.clarin.chronopress.presentation.page.samplebrowser;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;

@CDIView(SampleBrowserView.ID)
public class SampleBrowserViewImpl extends AbstractView<SampleBrowserViewPresenter> implements SampleBrowserView {

    @Inject
    private Instance<SampleBrowserViewPresenter> presenter;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    SampleTable table;

    @Inject
    SampleWindow window;

    private MTextField journal, article, author;
    private MDateField date;
    private MButton search;

    @PostConstruct
    public void init() {

        journal = new MTextField()
                .withStyleName(ValoTheme.TEXTFIELD_SMALL)
                .withFullWidth();

        article = new MTextField()
                .withStyleName(ValoTheme.TEXTFIELD_SMALL)
                .withFullWidth();

        author = new MTextField()
                .withStyleName(ValoTheme.TEXTFIELD_SMALL)
                .withFullWidth();

        date = new MDateField()
                .withStyleName(ValoTheme.DATEFIELD_SMALL)
                .withFullWidth();

        search = new MButton(FontAwesome.SEARCH)
                .withStyleName(ValoTheme.BUTTON_FRIENDLY, ValoTheme.BUTTON_SMALL)
                .withListener((Button.ClickEvent event) -> {
                    table.setBeans(new SortableLazyList<>(
                            // entity fetching strategy
                            (firstRow, asc, sortProperty) -> getPresenter().findEntities(
                                    firstRow, asc, sortProperty, journal.getValue(), date.getValue(), article.getValue(), author.getValue()),
                            // count fetching strategy
                            () -> (int) getPresenter().size(journal.getValue(), date.getValue(), article.getValue(), author.getValue()), 30));
                });

        MHorizontalLayout bar = new MHorizontalLayout()
                .withSpacing(true)
                .withMargin(new MMarginInfo(false, true))
                .with(journal, article, author, date, search)
                .withAlign(search, Alignment.BOTTOM_RIGHT)
                .withFullWidth();

        MVerticalLayout layout = new MVerticalLayout()
                .withSize(MSize.FULL_SIZE)
                .withMargin(new MMarginInfo(true, true))
                .withSpacing(true)
                .with(bar, table);
        search.click();
        setCompositionRoot(layout);
    }

    public void showSampleWindow(Sample s) {
        window.setItem(s);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        loadLabels();
    }

    private void loadLabels() {
        journal.setCaption(provider.getProperty("label.journal.title"));
        article.setCaption(provider.getProperty("label.article.title"));
        author.setCaption(provider.getProperty("label.authors"));
        date.setCaption(provider.getProperty("label.published.date"));
        search.setCaption(provider.getProperty("label.search"));
    }

    @Override
    protected SampleBrowserViewPresenter generatePresenter() {
        return presenter.get();
    }
}
