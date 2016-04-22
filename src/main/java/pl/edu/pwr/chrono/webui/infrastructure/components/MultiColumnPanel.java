package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.google.common.collect.Lists;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.util.List;

public class MultiColumnPanel extends VerticalLayout {

    private final List<Panel> panels;
    private final HorizontalLayout buttons;
    private final DbPropertiesProvider provider;
    private final VerticalLayout content = new VerticalLayout();
    private final VerticalLayout loading;

    private MultiColumnPanel(PanelBuilder builder, DbPropertiesProvider provider) {
        setSpacing(true);
        panels = builder.panels;
        buttons = builder.buttons;
        loading = builder.loading;
        this.provider = provider;

        content.setSpacing(true);
        content.setSizeUndefined();

        if (builder.columnTitle != null)
            content.addComponent(builder.columnTitle);

        HorizontalLayout panelRow = new HorizontalLayout();
        panels.forEach(panelRow::addComponent);
        content.addComponent(panelRow);

        addComponent(content);
        setComponentAlignment(content, Alignment.MIDDLE_CENTER);

        if (buttons != null) {
            buttons.setSpacing(true);
            buttons.setMargin(new MarginInfo(false, true, false, false));
            content.addComponent(buttons);
            content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
        }

    }

    public void showLoadingIndicator() {
        content.replaceComponent(buttons, loading);
    }

    public void hideLoadingIndicator() {
        content.replaceComponent(loading, buttons);
    }

    public static class PanelBuilder {

        private final List<Panel> panels;
        private HorizontalLayout columnTitle;
        private HorizontalLayout buttons;
        private VerticalLayout loading;
        private DbPropertiesProvider provider;

        public PanelBuilder(DbPropertiesProvider provider) {
            this.provider = provider;
            this.panels = Lists.newArrayList();
            loading = initializeLoading();
        }

        public PanelBuilder addColumnTitle(String title) {
            columnTitle = new HorizontalLayout();
            columnTitle.setWidth(100, Unit.PERCENTAGE);
            columnTitle.addComponent(new Title(title));
            return this;
        }

        public PanelBuilder addButton(Button btn) {
            if (buttons == null) {
                buttons = new HorizontalLayout();
            }
            btn.addStyleName(ValoTheme.BUTTON_SMALL);
            buttons.addComponent(btn);
            buttons.setComponentAlignment(btn, Alignment.MIDDLE_RIGHT);
            return this;
        }

        public PanelBuilder addPanel(String title, ContentBuilder builder) {
            Panel panel = new Panel();
            panel.setStyleName(ChronoTheme.RESULT_PANEL);
            panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
            panel.setCaption(title);
            panel.setContent(builder.content);
            panels.add(panel);
            return this;
        }

        private VerticalLayout initializeLoading() {
            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);

            ProgressBar progressBar = new ProgressBar();
            progressBar.setIndeterminate(true);

            HorizontalLayout loadingNotification = new HorizontalLayout();
            loadingNotification.setSpacing(true);
            loadingNotification.addComponents(progressBar, new Label(provider.getProperty("label.loading")));

            layout.addComponents(loadingNotification);
            layout.setComponentAlignment(loadingNotification, Alignment.MIDDLE_CENTER);

            return layout;
        }

        public MultiColumnPanel build() {
            return new MultiColumnPanel(this, provider);
        }

        public static class ContentBuilder {

            private List<Component> components = Lists.newArrayList();
            private AbstractOrderedLayout content;

            public ContentBuilder addLeadTitle(String title) {
                components.add(new Title(title, ChronoTheme.TITLE_PLAIN));
                return this;
            }

            public ContentBuilder addTitle(String title) {
                components.add(new Title(title));
                return this;
            }

            public ContentBuilder addComponentInForm(String caption, Component cmp) {
                FormLayout layout = new FormLayout();
                layout.addStyleName(ChronoTheme.COMPACT_FORM);
                applyStyle(cmp);
                cmp.setCaption(caption);
                layout.addComponent(cmp);
                components.add(layout);
                return this;
            }

            public ContentBuilder addComponentInFormWithHelp(String caption, Component cmp, Component help) {
                HorizontalLayout l = new HorizontalLayout();
                FormLayout layout = new FormLayout();
                layout.addStyleName(ChronoTheme.COMPACT_FORM);
                applyStyle(cmp);
                cmp.setCaption(caption);
                layout.addComponent(cmp);
                l.addComponent(layout);
                l.addComponent(help);
                components.add(l);
                return this;
            }

            public ContentBuilder addRow(String firstCaption, Component firstCmp,
                                         String secondCaption, Component secondCmp) {
                HorizontalLayout layout = new HorizontalLayout();
                applyStyle(firstCmp);
                firstCmp.setCaption(firstCaption);
                firstCmp.setWidth(150, Unit.PIXELS);
                applyStyle(secondCmp);
                secondCmp.setCaption(secondCaption);
                layout.addComponent(firstCmp);
                layout.addComponent(secondCmp);
                components.add(layout);
                return this;
            }

            public ContentBuilder addComponent(String caption, Component cmp) {
                applyStyle(cmp);
                cmp.setCaption(caption);
                components.add(cmp);
                return this;
            }

            public ContentBuilder addComponentWithHelp(String caption, Component cmp, Component help) {
                HorizontalLayout h = new HorizontalLayout();
                h.setCaption(caption);
                applyStyle(cmp);
                h.addComponent(cmp);
                h.addComponent(help);
                components.add(h);
                return this;
            }

            private void applyStyle(Component cmp) {
                if (cmp instanceof TextField) cmp.addStyleName(ValoTheme.TEXTFIELD_TINY);
                if (cmp instanceof CheckBox) cmp.addStyleName(ValoTheme.CHECKBOX_SMALL);
                if (cmp instanceof ComboBox) cmp.addStyleName(ValoTheme.COMBOBOX_TINY);
                if (cmp instanceof ComboBoxMultiselect) cmp.addStyleName(ValoTheme.COMBOBOX_TINY);
                if (cmp instanceof OptionGroup) cmp.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
            }

            public ContentBuilder build() {
                VerticalLayout layout = new VerticalLayout();
                layout.setMargin(true);
                components.forEach(layout::addComponent);
                content = layout;
                return this;
            }

            public ContentBuilder buildWithFromLayout() {
                FormLayout layout = new FormLayout();
                layout.addStyleName(ChronoTheme.COMPACT_FORM);
                layout.setMargin(true);
                components.forEach(layout::addComponent);
                content = layout;
                return this;
            }
        }
    }

}
