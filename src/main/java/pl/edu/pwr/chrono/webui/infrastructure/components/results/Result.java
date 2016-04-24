package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.google.common.collect.Maps;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.io.*;
import java.util.Map;

@SpringComponent
@ViewScope
public class Result extends VerticalLayout {

    private final Map<String, CalculationResult> results = Maps.newHashMap();
    CalculationResult calculation;
    @Autowired
    private DbPropertiesProvider provider;

    public Result() {
        setSpacing(true);
        setMargin(new MarginInfo(false, true, false, true));
    }

    public void setCalculation(CalculationResult calculation) {
        this.calculation = calculation;
        if (!results.containsKey(calculation.getType())) {
            results.put(calculation.getType(), calculation);
        }
    }

    public Boolean hasResult(String str) {
        return results.containsKey(str);
    }

    public CalculationResult getResult(String type) {
        return results.get(type);
    }

    public void show() {
        Component panel = createContentWrapper(calculation.showResult(), calculation.getType());
        addComponent(panel);
    }

    private Component createContentWrapper(final Component content, String type) {
        final CssLayout slot = new CssLayout();
        slot.setId(type);
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

        MenuBar.MenuItem root = tools.addItem("", FontAwesome.TIMES_CIRCLE, (MenuBar.Command) selectedItem -> {
            results.remove(slot.getId());
            this.removeComponent(slot);
        });

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        content.addStyleName(ChronoTheme.PANEL_CONTENT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

}
