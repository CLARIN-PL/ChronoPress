package pl.edu.pwr.chrono.webui.ui.admin.lexicalfield;

import com.google.common.collect.Sets;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.domain.LexicalField;
import pl.edu.pwr.chrono.webui.infrastructure.DefaultView;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.components.EntityComboBox;
import pl.edu.pwr.chrono.webui.infrastructure.components.Title;
import pl.edu.pwr.chrono.webui.ui.main.MainUI;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@SpringView(name = LexicalFieldView.VIEW_NAME, ui = MainUI.class)
@UIScope
public class LexicalFieldView extends DefaultView<LexicalFieldPresenter> implements View {

    public static final String VIEW_NAME = "lexical-field";
    private final Grid grid = new Grid();
    private final EntityComboBox<LexicalField> lexicalFields = new EntityComboBox("groupName", LexicalField.class);
    @Autowired
    private DbPropertiesProvider provider;
    private BeanItemContainer<NameItem> container = new BeanItemContainer<>(NameItem.class);

    @Autowired
    private LexicalFieldWindow window;

    @Autowired
    public LexicalFieldView(LexicalFieldPresenter presenter) {
        super(presenter);
    }

    @PostConstruct
    public void init() {
        addComponent(new Title(provider.getProperty("view.admin.lexical.field.title")));
        setSpacing(true);

        initButtons();
        iniGridColumns();
        HorizontalLayout buttons = buildButtons();
        addComponent(buttons);
        addComponent(buildLexicalFieldSelection());

        addComponent(grid);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(10);
    }

    private void initButtons() {
        window.getCancel().addClickListener(event -> {
            UI.getCurrent().removeWindow(window);
        });
        window.getSave().addClickListener(event -> {
            presenter.save(window.getItem());
            loadLexicalNames();
            UI.getCurrent().removeWindow(window);
        });
    }

    private HorizontalLayout buildButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button createLexical = new Button(provider.getProperty("button.create.lexical.group"));
        createLexical.addStyleName(ValoTheme.BUTTON_TINY);
        createLexical.addStyleName(ChronoTheme.BUTTON);
        createLexical.setIcon(FontAwesome.PLUS_CIRCLE);
        createLexical.addClickListener(event -> {
            window.setItem(new LexicalField());
            UI.getCurrent().addWindow(window);
        });

        Button deleteLexical = new Button(provider.getProperty("button.delete.lexical.group"));
        deleteLexical.addStyleName(ValoTheme.BUTTON_TINY);
        deleteLexical.addStyleName(ChronoTheme.BUTTON);
        deleteLexical.setIcon(FontAwesome.TRASH_O);
        deleteLexical.addClickListener(event -> {
            LexicalField item = lexicalFields.getContainer().getItem(lexicalFields.getValue()).getBean();
            presenter.delete(item);
            loadLexicalNames();
        });

        buttons.addComponent(createLexical);
        buttons.addComponent(deleteLexical);
        return buttons;
    }

    public HorizontalLayout buildNameSection() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        TextField name = new TextField();
        name.addStyleName(ValoTheme.TEXTAREA_TINY);

        Label label = new Label(provider.getProperty("label.name"));

        Button addName = new Button(provider.getProperty("label.add.lexical.name"));
        addName.setIcon(FontAwesome.PLUS_CIRCLE);
        addName.addStyleName(ValoTheme.BUTTON_TINY);
        addName.addStyleName(ChronoTheme.BUTTON);

        addName.addClickListener(event -> {
            if (lexicalFields.getValue() != null && !name.getValue().equals("")) {
                container.addBean(new NameItem(Integer.toUnsignedLong(container.getItemIds().size() + 1), name.getValue()));
                LexicalField item = lexicalFields.getContainer().getItem(lexicalFields.getValue()).getBean();
                Set<String> names = item.getLexicalnames();
                names.add(name.getValue());
                item.setLexicalnames(Sets.newHashSet());
                presenter.save(item);
                item.setLexicalnames(names);
                presenter.save(item);
                name.setValue("");
            }
        });

        Button removeSelected = new Button(provider.getProperty("label.remove.lexical.name"));
        removeSelected.setIcon(FontAwesome.TRASH_O);
        removeSelected.addStyleName(ValoTheme.BUTTON_TINY);
        removeSelected.addStyleName(ChronoTheme.BUTTON);
        removeSelected.addClickListener(event -> {
            if (grid.getSelectedRows().size() > 0) {
                LexicalField item = lexicalFields.getContainer().getItem(lexicalFields.getValue()).getBean();
                Set<String> names = item.getLexicalnames();
                grid.getSelectedRows().forEach(o -> {
                    names.remove(((NameItem) o).getName());
                });
                item.setLexicalnames(Sets.newHashSet());
                presenter.save(item);
                item.setLexicalnames(names);
                presenter.save(item);
                populateGrid(item);
            }
        });

        layout.addComponent(label);
        layout.addComponent(name);
        layout.addComponent(addName);
        layout.addComponent(removeSelected);

        return layout;
    }

    public HorizontalLayout buildLexicalFieldSelection() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.addComponent(new Label(provider.getProperty("label.lexical.field.group")));
        layout.addComponent(lexicalFields);

        lexicalFields.addStyleName(ValoTheme.COMBOBOX_TINY);
        lexicalFields.addValueChangeListener(event -> {
            populateGrid(lexicalFields.getContainer().getItem(event.getProperty().getValue()).getBean());
        });

        layout.addComponent(buildNameSection());
        return layout;
    }

    private void populateGrid(LexicalField field) {
        final long[] id = {1};
        grid.getSelectionModel().reset();
        container.removeAllItems();
        field.getLexicalnames().forEach(f ->
                        container.addBean(new NameItem(id[0]++, f))
        );
    }

    private void iniGridColumns() {
        grid.setContainerDataSource(container);
        grid.getColumn("id").setHidden(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void loadLexicalNames() {
        lexicalFields.getContainer().removeAllItems();
        List<LexicalField> items = presenter.getLexicalFields();
        lexicalFields.load(items);
        lexicalFields.setNullSelectionAllowed(false);
        if (presenter.getLexicalFields().size() > 0) lexicalFields.select(items.get(0).getId());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        loadLexicalNames();
    }

    @Data
    public class NameItem {
        private Long id;
        private String name;

        public NameItem() {
        }

        public NameItem(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}
