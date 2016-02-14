package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

@SpringComponent
@ViewScope
public class Result extends VerticalLayout {

    CalculationResult calculation;

    @Autowired
    private DbPropertiesProvider provider;

    public Result() {
        setSpacing(true);
        setMargin(new MarginInfo(false, true, false, true));
    }

    public void setCalculation(CalculationResult calculation) {
        this.calculation = calculation;
    }

    public void show() {
        Component panel = createContentWrapper(calculation.showResult());
        addComponent(panel);
    }

    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName(ChronoTheme.PANEL_TOOLBAR);
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

        MenuBar.MenuItem root = tools.addItem("", FontAwesome.COG, null);
        root.addItem(provider.getProperty("label.export"), (MenuBar.Command) selectedItem -> Notification.show("Not implemented"));
        root.addSeparator();
        root.addItem(provider.getProperty("label.close"), (MenuBar.Command) selectedItem -> this.removeComponent(slot));

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        content.addStyleName(ChronoTheme.PANEL_CONTENT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }
}
