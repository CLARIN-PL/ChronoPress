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
import pl.clarin.chronopress.presentation.shered.layout.Title;
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

    @PostConstruct
    public void init() {

        MTextField journal = new MTextField(provider.getProperty("label.journal.title"))
                .withStyleName(ValoTheme.TEXTFIELD_SMALL)
                .withFullWidth();

        MTextField article = new MTextField(provider.getProperty("label.article.title"))
                .withStyleName(ValoTheme.TEXTFIELD_SMALL)
                .withFullWidth();

        MTextField author = new MTextField(provider.getProperty("label.authors"))
                .withStyleName(ValoTheme.TEXTFIELD_SMALL)
                .withFullWidth();

        MDateField date = new MDateField(provider.getProperty("label.published.date"))
                .withStyleName(ValoTheme.DATEFIELD_SMALL)
                .withFullWidth();

        MButton search = new MButton(FontAwesome.SEARCH)
                .withCaption(provider.getProperty("label.search"))
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
                .withMargin(new MMarginInfo(false, true))
                .withSpacing(true)
                .with(new Title(FontAwesome.BOOK, provider.getProperty("view.sample.browser.title")),
                        bar, table);
        search.click();
        setCompositionRoot(layout);
    }

    public void showSampleWindow(Sample s) {
        window.setItem(s);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

    @Override
    protected SampleBrowserViewPresenter generatePresenter() {
        return presenter.get();
    }
}
