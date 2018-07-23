/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.lexicalfield;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashSet;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.lexicalfield.entity.LexicalField;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

public class LexicalFieldGrid extends CustomComponent {

    @Inject
    DbPropertiesProvider provider;
    private Grid grid;
    private LexicalField current;

    @Inject
    javax.enterprise.event.Event<SaveLexicalFieldEvent> saveEvent;

    private BeanItemContainer<NameItem> container = new BeanItemContainer<>(NameItem.class);

    @PostConstruct
    public void init() {

        grid = new MGrid()
                .withSize(MSize.FULL_SIZE)
                .withStyleName(ChronoTheme.GRID)
                .withProperties("name");

        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(10);
        grid.setContainerDataSource(container);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        VerticalLayout layout = new MVerticalLayout()
                .withFullWidth()
                .with(buildNameSection(), grid);

        setCompositionRoot(layout);
    }

    public HorizontalLayout buildNameSection() {

        TextField name = new MTextField()
                .withStyleName(ValoTheme.TEXTAREA_TINY);

        Label label = new Label(provider.getProperty("label.name"));

        Button addName = new MButton()
                .withCaption(provider.getProperty("label.add.lexical.name"))
                .withIcon(FontAwesome.PLUS_CIRCLE)
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withListener(l -> {
                    if (current != null && !name.getValue().equals("")) {
                        addItem(name.getValue());
                        current.getLexicalnames().add(name.getValue());
                        saveEvent.fire(new SaveLexicalFieldEvent(current));
                        name.setValue("");
                    }
                });

        Button removeSelected = new MButton()
                .withCaption(provider.getProperty("label.remove.lexical.name"))
                .withIcon(FontAwesome.TRASH_O)
                .withStyleName(ValoTheme.BUTTON_TINY, ChronoTheme.BUTTON)
                .withListener(l -> {
                    if (grid.getSelectedRows().size() > 0) {
                        grid.getSelectedRows().forEach(o -> {
                            current.getLexicalnames().remove(((NameItem) o).getName());
                            container.removeItem(o);
                        });
                        grid.getSelectionModel().reset();
                        saveEvent.fire(new SaveLexicalFieldEvent(current));
                    }
                });

        return new MHorizontalLayout()
                .withSpacing(true)
                .with(label, name, addName, removeSelected);
    }

    public void addItem(String item) {
        container.addBean(new NameItem(Integer.toUnsignedLong(container.getItemIds().size() + 1), item));
    }

    public void setLexicalField(LexicalField field) {
        this.current = field;
        if (current != null) {
            if (current.getLexicalnames() == null) {
                current.setLexicalnames(new HashSet<>());
            }

            final long[] id = {1};
            cleanUp();
            current.getLexicalnames().forEach(f
                    -> container.addBean(new NameItem(id[0]++, f))
            );

        }
    }

    public void cleanUp() {
        grid.getSelectionModel().reset();
        container.removeAllItems();
    }
}
