package pl.clarin.chronopress.presentation.page.admin.lexicalfield;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.layout.MComboBox;
import pl.clarin.chronopress.presentation.shered.layout.Title;
import pl.clarin.chronopress.presentation.shered.mvp.AbstractView;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@CDIView(LexicalFieldView.ID)
@RolesAllowed({"moderator"})
public class LexicalFieldViewImpl extends AbstractView<LexicalFieldViewPresenter> implements LexicalFieldView {

    @Inject
    private Instance<LexicalFieldViewPresenter> presenterInstance;

    private final MComboBox<LexicalField> lexicalFields = new MComboBox("groupName", LexicalField.class);

    @Override
    protected LexicalFieldViewPresenter generatePresenter() {
        return presenterInstance.get();
    }

    @Inject
    LexicalFieldWindow window;

    @Inject
    DbPropertiesProvider provider;

    @Inject
    LexicalFieldGrid grid;
    
    @Inject
    javax.enterprise.event.Event<DeleteLexicalFieldEvent> deleteEvent;

    @PostConstruct
    public void init() {

        VerticalLayout layout = new MVerticalLayout()
                .withSpacing(true)
                .with(new Title(provider.getProperty("view.admin.lexical.field.title")),
                        buildGroupSection(), grid);
        
        setCompositionRoot(layout);
    }

    private HorizontalLayout buildGroupSection() {

        lexicalFields.addStyleName(ValoTheme.COMBOBOX_TINY);
        lexicalFields.addValueChangeListener(event -> {
            grid.setLexicalField(lexicalFields.getBean());
        });
        
        Button createLexical = new MButton()
                .withCaption(provider.getProperty("button.create.lexical.group"))
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withIcon(FontAwesome.PLUS_CIRCLE)
                .withListener(l -> {
                    window.setLexicalField(new LexicalField());
                    UI.getCurrent().addWindow(window);
                });

        Button deleteLexical = new MButton()
                .withCaption(provider.getProperty("button.delete.lexical.group"))
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withIcon(FontAwesome.TRASH_O)
                .withListener(l -> {
                    deleteEvent.fire(new DeleteLexicalFieldEvent(lexicalFields.getBean()));
                });

        return new MHorizontalLayout()
                .withSpacing(true)
                .with(new Label(provider.getProperty("label.lexical.field.group")), lexicalFields, createLexical, deleteLexical);

    }
    
    @Override
    public void swapLexicalName(LexicalField old, LexicalField modified){
        lexicalFields.removeBean(old);
        lexicalFields.addBean(modified);
    }
    
    @Override
    public void setLexicalNames(List<LexicalField> list) {
        lexicalFields.addBeans(list);
        lexicalFields.setNullSelectionAllowed(false);
        if (list.size() > 0) {
            lexicalFields.select(list.get(0).getId());
        }
    }

    @Override
    public void removeLexicalName(LexicalField filed) {
        lexicalFields.removeWithUnselect(filed);
        grid.cleanUp();
    }

}
