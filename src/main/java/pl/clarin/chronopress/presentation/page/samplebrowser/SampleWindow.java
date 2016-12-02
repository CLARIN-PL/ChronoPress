package pl.clarin.chronopress.presentation.page.samplebrowser;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.sample.entity.ProcessingStatus;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class SampleWindow extends Window implements Property.ValueChangeListener {

    @PropertyId("journalTitle")
    private final TextField journalTitle = new TextField();

    @PropertyId("articleTitle")
    private final TextField articleTitle = new TextField();

    @PropertyId("authors")
    private final TextField authors = new TextField();

    @PropertyId("style")
    private final TextField style = new TextField();

    @PropertyId("medium")
    private final TextField medium = new TextField();

    @PropertyId("period")
    private final TextField period = new TextField();

    @PropertyId("exposition")
    private final TextField exposition = new TextField();

    @PropertyId("txt")
    private final RichTextArea text = new RichTextArea();

    @PropertyId("date")
    private final DateField publishedDate = new DateField();

    @PropertyId("fileName")
    private final TextField filename = new TextField();

    @PropertyId("processingStatus")
    private final ComboBox processingStatus = new ComboBox("Status próbki", Arrays.asList(ProcessingStatus.values()));

    private final BeanFieldGroup<Sample> binder = new BeanFieldGroup<>(Sample.class);

    @Inject
    javax.enterprise.event.Event<SaveSampleEvent> saveEvent;

    @Inject
    DbPropertiesProvider provider;

    private Button save;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.text"));
        setWidth(80, Unit.PERCENTAGE);
        setHeight(80, Unit.PERCENTAGE);
        addStyleName(ChronoTheme.WINDOW);
        binder.bindMemberFields(this);
        setModal(true);
        center();
        setContent(buildForm());
    }

    public void setItem(Sample t) {
        binder.setReadOnly(false);
        binder.setItemDataSource(t);
        if (!SecurityUtils.getSubject().hasRole("moderator") || !SecurityUtils.getSubject().hasRole("edytor")) {
            binder.setReadOnly(true);
        }
        if (save
                != null) {
            save.setEnabled(false);
        }
    }

    public void setItemWithMarkedkWord(Sample txt, String word) {
        String t = txt.getTxt().replace(word, "<b>" + word + "</b>");
        txt.setTxt(t);
        setItem(txt);
    }

    private Component buildForm() {

        articleTitle.setCaption(provider.getProperty("label.article.title"));

        journalTitle.setCaption(provider.getProperty("label.journal.title"));
        journalTitle.addValueChangeListener(this);

        authors.setCaption(provider.getProperty("label.author"));
        authors.setNullRepresentation("");
        authors.addValueChangeListener(this);

        publishedDate.setCaption(provider.getProperty("label.published.date"));
        publishedDate.addValueChangeListener(this);

        filename.setCaption(provider.getProperty("label.filename"));
        filename.addValueChangeListener(this);

        MFormLayout left = new MFormLayout()
                .withFullWidth()
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT);
        left.addComponents(articleTitle, journalTitle, publishedDate, authors);

        period.setCaption(provider.getProperty("label.period"));
        period.addValueChangeListener(this);

        style.setCaption(provider.getProperty("label.style"));
        style.addValueChangeListener(this);

        medium.setCaption(provider.getProperty("label.medium"));
        medium.addValueChangeListener(this);

        exposition.setCaption(provider.getProperty("label.exposition"));
        exposition.setNullRepresentation("");
        exposition.addValueChangeListener(this);

        text.setSizeFull();

        text.addValueChangeListener(l -> {
            if (save != null) {
                save.setEnabled(true);
                processingStatus.setValue(ProcessingStatus.TO_PROCESS);
            }
        });

        MFormLayout right = new MFormLayout()
                .withFullWidth()
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT);
        right.addComponents(period, style, medium, exposition, new HorizontalLayout());

        MHorizontalLayout wrapper = new MHorizontalLayout()
                .withWidth(100, Unit.PERCENTAGE)
                .with(left, right)
                .withExpand(left, 2)
                .withExpand(right, 1);

        MVerticalLayout content = new MVerticalLayout()
                .withFullWidth()
                .withFullHeight()
                .withMargin(true)
                .withSpacing(true)
                .with(wrapper, text)
                .withExpand(text, 1);

        if (!SecurityUtils.getSubject().hasRole("moderator") || !SecurityUtils.getSubject().hasRole("edytor")) {
            content.add(buildEdytorSection());
            content.addComponent(buildButtons());
        }

        return content;
    }

    private FormLayout buildEdytorSection() {
        FormLayout frm = new MFormLayout()
                .withFullWidth()
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT);
        frm.addComponents(filename, processingStatus);
        return frm;
    }

    private HorizontalLayout buildButtons() {

        save = new MButton()
                .withCaption(provider.getProperty("button.save"))
                .withStyleName(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL)
                .withIcon(FontAwesome.SAVE)
                .withListener(l -> {
                    try {
                        binder.commit();
                        saveEvent.fire(new SaveSampleEvent(binder.getItemDataSource().getBean()));
                        close();

                    } catch (FieldGroup.CommitException ex) {
                        Logger.getLogger(SampleWindow.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                });

        final Button cancel = new MButton()
                .withCaption(provider.getProperty("button.cancel"))
                .withStyleName(ValoTheme.BUTTON_SMALL)
                .withIcon(FontAwesome.TIMES)
                .withListener(l -> {
                    binder.discard();
                    close();
                });

        MHorizontalLayout wrapper = new MHorizontalLayout()
                .withSpacing(true)
                .with(save, cancel);

        return new MHorizontalLayout()
                .withFullWidth()
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .with(wrapper)
                .withAlign(wrapper, Alignment.MIDDLE_RIGHT);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (save != null) {
            save.setEnabled(true);
        }
    }
}
