package pl.clarin.chronopress.presentation.page.education;

import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.business.education.entity.TitleDTO;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class CategoryTab extends HorizontalLayout {

    @Inject
    PageTitlesTable pageTitlesTable;

    private final VerticalLayout content = new VerticalLayout();

    @Inject
    DbPropertiesProvider provider;

    @Inject
    EducationViewPresenter presenter;

    @PostConstruct
    public void init() {

        MVerticalLayout layout = new MVerticalLayout()
                .withFullHeight()
                .withFullWidth()
                .with(content);

        MPanel panel = new MPanel()
                .withStyleName(ValoTheme.PANEL_BORDERLESS, ValoTheme.PANEL_SCROLL_INDICATOR)
                .withFullHeight()
                .withFullWidth()
                .withContent(layout);

        addStyleName(ChronoTheme.PAGE_TAB);
        setMargin(new MarginInfo(true, true, true, false));
        setSpacing(true);
        setSizeFull();
        addComponents(pageTitlesTable, panel);
        setExpandRatio(pageTitlesTable, 1.5f);
        setExpandRatio(panel, 4f);

        pageTitlesTable.setRowClickListener(event -> {
            loadPage(event.getEntity().getId());
        });
    }

    private void loadPage(Long id) {
        EducationPage page = presenter.getEducationPageById(id);
        buildContent(page);
    }

    private void buildContent(EducationPage page) {

        content.removeAllComponents();

        Accordion accordion = new Accordion();

        if (page.getDiscursive() != null && !"".equals(page.getDiscursive())) {
            if (page.getTabular() != null && !"".equals(page.getTabular())) {
                accordion.addComponent(createContentLabel(provider.getProperty("label.discursive"), page.getDiscursive()));
            } else {
                content.addComponent(createContentLabel(null, page.getDiscursive()));
            }
        }

        if (page.getTabular() != null && !"".equals(page.getTabular())) {
            accordion.addComponent(createContentLabel(provider.getProperty("label.tabular"), page.getTabular()));
        }

        if (page.getTabular() != null && !"".equals(page.getTabular())) {
            accordion.addComponent(createContentLabel(provider.getProperty("label.citation"), page.getCitation()));
        }

        if (page.getFilename() != null && !"".equals(page.getFilename())) {
            File file = new File("/tmp/chronopress/uploads/images/" + page.getFilename());
            if (file.exists() && file.isFile()) {
                Image img = new Image();
                img.setSource(new FileResource(file));

                MHorizontalLayout layout = new MHorizontalLayout()
                        .withCaption(provider.getProperty("label.image"))
                        .withFullWidth()
                        .with(img).withAlign(img, Alignment.MIDDLE_CENTER);
                accordion.addComponent(layout);
            }
        }

        if (accordion.getComponentCount() > 0) {
            content.addComponent(accordion);
        }
    }

    private Label createContentLabel(String title, String content) {
        return new MLabel()
                .withCaption(title)
                .withContentMode(ContentMode.HTML)
                .withFullHeight()
                .withFullWidth()
                .withContent(content);
    }

    public void setCategory(String title) {
        setCaption(title);
    }

    public void setTitles(List<TitleDTO> list) {
        pageTitlesTable.setCategories(list);
        if (list.size() > 0) {
            loadPage(list.get(0).getId());
        }
    }

}
