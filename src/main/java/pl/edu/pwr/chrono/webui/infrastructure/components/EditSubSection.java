package pl.edu.pwr.chrono.webui.infrastructure.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by tnaskret on 01.01.16.
 */
public class EditSubSection extends VerticalLayout {

    private EditSubSection(boolean withHeader, EditSubSectionBuilder builder) {
        addStyleName(ChronoTheme.DETAILS_CONTENT);
        setSizeFull();
        if (withHeader) {
            addComponent(builder.header);
        }
        addComponent(builder.body);
    }

    public static class EditSubSectionBuilder {

        private final HorizontalLayout header;
        private final VerticalLayout body;
        private HorizontalLayout buttons;

        public EditSubSectionBuilder() {
            this.header = new HorizontalLayout();
            header.setWidth(100f, Unit.PERCENTAGE);
            header.setSpacing(true);

            this.body = new VerticalLayout();
            body.setSizeFull();
        }

        public EditSubSectionBuilder addHeaderTitle(String title) {

            HorizontalLayout wrapper = new HorizontalLayout();
            wrapper.addStyleName(ChronoTheme.DETAILS_SECTION_TITLE);
            wrapper.addComponent(new Label(title));
            header.addComponent(wrapper);
            header.setComponentAlignment(wrapper, Alignment.TOP_LEFT);
            return this;
        }


        public EditSubSectionBuilder addHeaderButton(Button btn) {
            if (buttons == null) {
                this.buttons = new HorizontalLayout();
                header.addComponent(buttons);
                header.setExpandRatio(buttons, 1.0f);
            }

            btn.addStyleName(ValoTheme.BUTTON_TINY);
            btn.addStyleName(ChronoTheme.BUTTON);
            buttons.addComponent(btn);
            buttons.setComponentAlignment(btn, Alignment.TOP_RIGHT);
            return this;
        }

        public EditSubSectionBuilder addCustomElementToHeader(Component comp) {
            buttons.addComponent(comp);
            buttons.setComponentAlignment(comp, Alignment.TOP_RIGHT);
            return this;
        }

        public EditSubSectionBuilder addCustomElement(Component comp, Alignment alignment) {
            body.addComponent(comp);
            body.setComponentAlignment(comp, alignment);
            return this;
        }

        public EditSubSectionBuilder addRow(FieldBuilder builder) {
            body.addComponent(builder);
            return this;
        }


        public EditSubSectionBuilder addForm(FormLayoutBuilder form) {
            body.addComponent(form);
            return this;
        }

        public EditSubSectionBuilder addRowWithMargin(FieldBuilder builder) {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setSizeFull();

            VerticalLayout margin = new VerticalLayout();
            margin.setWidth(130f, Unit.PIXELS);

            layout.addComponent(margin);
            layout.addComponent(builder);
            layout.setExpandRatio(builder, 1.0f);

            body.addComponent(layout);

            return this;
        }


        public EditSubSection build() {
            return new EditSubSection(true, this);
        }

        public EditSubSection buildWithoutHeader() {
            return new EditSubSection(false, this);
        }
    }

    public static class FormLayoutBuilder extends FormLayout {

        public FormLayoutBuilder() {
            setSpacing(true);
        }

        public FormLayoutBuilder addPasswordField(PasswordField field, String caption) {
            field.setCaption(caption);
            field.addStyleName(ValoTheme.TEXTFIELD_TINY);
            addComponent(field);
            return this;
        }

        public FormLayoutBuilder build() {
            return this;
        }
    }

    public static class FieldBuilder extends HorizontalLayout {

        public FieldBuilder() {
            setHeight("100%");
            setSpacing(true);
        }

        public FieldBuilder addField(final String caption, final TextField field) {
            field.setCaption(caption);
            field.addStyleName(ValoTheme.TEXTFIELD_TINY);
            field.setNullRepresentation("");
            field.setImmediate(true);
            addComponent(field);
            setComponentAlignment(field, Alignment.TOP_LEFT);
            return this;
        }

        public FieldBuilder addComboBox(final String caption, final ComboBox cmb) {
            cmb.addStyleName(ValoTheme.COMBOBOX_TINY);
            cmb.setCaption(caption);
            addComponent(cmb);
            setComponentAlignment(cmb, Alignment.TOP_LEFT);
            return this;
        }

        public FieldBuilder addRequiredField(final String caption, final TextField field) {
            field.setCaption(caption);
            field.addStyleName(ValoTheme.TEXTFIELD_TINY);
            field.setRequired(true);
            field.setImmediate(true);
            field.setNullRepresentation("");
            field.setRequiredError("This is required field.");
            addComponent(field);
            setComponentAlignment(field, Alignment.TOP_LEFT);
            return this;
        }

        public FieldBuilder addButton(final String caption, final FontAwesome icon, final Button btn) {
            btn.setCaption(caption);
            btn.setIcon(icon);
            btn.addStyleName(ValoTheme.BUTTON_TINY);
            addComponent(btn);
            setComponentAlignment(btn, Alignment.TOP_LEFT);
            return this;
        }


        public FieldBuilder addFieldWithLeftLabel(String caption, TextField field) {
            FormLayout form = new FormLayout();
            field.setCaption(caption);
            field.addStyleName(ValoTheme.TEXTFIELD_TINY);
            field.setNullRepresentation("");
            field.setImmediate(true);
            form.addComponent(field);
            addComponent(form);
            setComponentAlignment(form, Alignment.TOP_LEFT);
            return this;
        }

        public FieldBuilder addPasswordFieldWithLeftLabel(String caption, PasswordField field) {
            FormLayout form = new FormLayout();
            field.setCaption(caption);
            field.addStyleName(ValoTheme.TEXTFIELD_TINY);
            field.setNullRepresentation("");
            field.setImmediate(true);
            form.addComponent(field);
            addComponent(form);
            setComponentAlignment(form, Alignment.TOP_LEFT);
            return this;
        }

        public FieldBuilder fieldsInColumn(ColumnBuilder column) {
            addComponent(column);
            return this;
        }

        public FieldBuilder build() {
            return this;
        }
    }

    public static class ColumnBuilder extends VerticalLayout {

        public ColumnBuilder() {
            setSizeFull();
        }

        public ColumnBuilder addRowWithDescription(final TextField field, final FontAwesome icon, final String description) {
            HorizontalLayout row = new HorizontalLayout();
            row.setHeight("100%");
            field.addStyleName(ValoTheme.TEXTFIELD_TINY);
            field.setColumns(10);
            field.setNullRepresentation("");
            field.setImmediate(true);
            row.setSpacing(true);
            field.setIcon(icon);

            FormLayout form = new FormLayout();
            form.addComponent(field);

            Label lbl = new Label(description);
            row.addComponent(form);
            row.addComponent(lbl);
            addComponent(row);
            return this;
        }

        public ColumnBuilder build() {
            return this;
        }
    }

}
