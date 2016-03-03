package pl.edu.pwr.chrono.webui.ui.samplebrowser;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.Text;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class TextWindow extends Window {

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
    @PropertyId("fileName")
    private final TextField fileName = new TextField();
    @PropertyId("txt")
    private final RichTextArea text = new RichTextArea();
    @PropertyId("date")
    private final DateField publishedDate = new DateField();
    private final BeanFieldGroup<Text> binder = new BeanFieldGroup<>(Text.class);
    @Autowired
    private DbPropertiesProvider provider;

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

    public void setItem(Text t) {
        binder.setReadOnly(false);
        binder.setItemDataSource(t);
        binder.setReadOnly(true);
        text.setReadOnly(false);
    }

    private Component buildForm() {

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);
        content.setSpacing(true);

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setWidth(100, Unit.PERCENTAGE);
        FormLayout left = new FormLayout();
        left.addStyleName(ChronoTheme.COMPACT_FORM);
        left.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        left.setSizeFull();
        wrapper.addComponent(left);

        FormLayout right = new FormLayout();
        right.addStyleName(ChronoTheme.COMPACT_FORM);
        right.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        right.setSizeFull();
        wrapper.addComponent(right);
        wrapper.setExpandRatio(left, 2);
        wrapper.setExpandRatio(right, 1);
        content.addComponent(wrapper);

        articleTitle.setCaption(provider.getProperty("label.article.title"));
        left.addComponent(articleTitle);

        journalTitle.setCaption(provider.getProperty("label.journal.title"));
        left.addComponent(journalTitle);

        authors.setCaption(provider.getProperty("label.author"));
        left.addComponent(authors);

        publishedDate.setCaption(provider.getProperty("label.published.date"));
        left.addComponent(publishedDate);

        period.setCaption(provider.getProperty("label.period"));
        right.addComponent(period);

        style.setCaption(provider.getProperty("label.style"));
        right.addComponent(style);

        medium.setCaption(provider.getProperty("label.medium"));
        right.addComponent(medium);

        exposition.setCaption(provider.getProperty("label.exposition"));
        exposition.setNullRepresentation("");
        right.addComponent(exposition);

        fileName.setCaption(provider.getProperty("label.filename"));
        left.addComponent(fileName);

        right.addComponent(new HorizontalLayout());

        text.setSizeFull();
        content.addComponent(text);
        content.setExpandRatio(text, 1);

        return content;
    }
}
