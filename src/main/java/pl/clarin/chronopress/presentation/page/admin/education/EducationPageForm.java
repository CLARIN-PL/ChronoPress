/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.education;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class EducationPageForm extends CustomComponent {

    @PropertyId("category")
    private final MTextField category = new MTextField();

    @PropertyId("pageTitle")
    private final MTextField pageTitle = new MTextField();

    @PropertyId("discursive")
    private final RichTextArea discursive = new RichTextArea();

    @PropertyId("tabular")
    private final RichTextArea tabular = new RichTextArea();

    @PropertyId("citation")
    private final RichTextArea citation = new RichTextArea();

    @PropertyId("published")
    private final CheckBox published = new CheckBox();

    private final BeanFieldGroup<EducationPage> binder = new BeanFieldGroup<>(EducationPage.class);

    @Inject
    private ImageUploader imageUploader;
    
    @Inject
    DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        binder.bindMemberFields(this);

        category.setCaption(provider.getProperty("label.category"));
        category.setWidth(100, Unit.PERCENTAGE);

        pageTitle.setCaption(provider.getProperty("label.page.title"));
        pageTitle.setWidth(100, Unit.PERCENTAGE);

        TabSheet sheet = new TabSheet();
        sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        sheet.setSizeFull();

        discursive.setCaption(provider.getProperty("label.discursive"));
        discursive.setWidth(100, Unit.PERCENTAGE);
        discursive.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        discursive.setNullRepresentation("");

        tabular.setCaption(provider.getProperty("label.tabular"));
        tabular.setWidth(100, Unit.PERCENTAGE);
        tabular.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        tabular.setNullRepresentation("");

        citation.setCaption(provider.getProperty("label.citation"));
        citation.setWidth(100, Unit.PERCENTAGE);
        citation.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        citation.setNullRepresentation("");


        MVerticalLayout image = new MVerticalLayout()
                .withFullWidth()
                .withFullHeight()
                .with(imageUploader)
                .withCaption(provider.getProperty("label.education.image"));

        sheet.addComponents(discursive, tabular, citation, image);

        published.setCaption(provider.getProperty("label.published"));

        MFormLayout form = new MFormLayout()
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT)
                .withFullWidth();

        form.addComponents(category, pageTitle, published);

        MVerticalLayout layout = new MVerticalLayout()
                .with(form, sheet)
                .withExpand(sheet, 1)
                .withFullHeight()
                .withFullWidth();

        setCompositionRoot(layout);
    }

    public EducationPage getPage() {
        try {
            binder.commit();
            EducationPage page = binder.getItemDataSource().getBean();
            page.setFilename(imageUploader.getFilename());
            return page;
        } catch (FieldGroup.CommitException e) {
            log.debug("Validation failed", e);
        }
        return binder.getItemDataSource().getBean();
    }

    public void setPage(EducationPage page) {
        binder.setItemDataSource(page);
        imageUploader.setImage(page.getFilename());
    }

    public void cleanUp() {
        binder.discard();
        binder.clear();
        imageUploader.cleanUp();
    }
}
