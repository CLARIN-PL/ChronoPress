package pl.clarin.chronopress.presentation.page.admin.education;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.education.entity.EducationPage;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class EducationPageWindow extends Window {

    @Inject
    DbPropertiesProvider provider;

    @Inject
    EducationPageForm form;
    
    @Inject
    javax.enterprise.event.Event<SaveEducationPageEvent> saveEducationPageEvent;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.page"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(70, Unit.PERCENTAGE);
        setModal(true);
        setContent(buildForm());
        center();
    }

    private HorizontalLayout buildButtons() {

        final MButton save = new MButton(provider.getProperty("button.save"))
                .withIcon(FontAwesome.SAVE)
                .withStyleName(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL)
                .withListener(e -> {
                    saveEducationPageEvent.fire(new SaveEducationPageEvent(form.getPage()));
                    cleanUpAndClose();
                });

        final MButton cancel = new MButton(provider.getProperty("button.cancel"))
                .withIcon(FontAwesome.TIMES)
                .withStyleName(ValoTheme.BUTTON_SMALL)
                .withListener(e -> {
                    cleanUpAndClose();
                });

        return new MHorizontalLayout()
                .withSpacing(true)
                .with(save, cancel);
    }

    private void cleanUpAndClose() {
        form.cleanUp();
        close();
    }

    private Component buildForm() {

        HorizontalLayout wrapper = buildButtons();

        MHorizontalLayout buttons = new MHorizontalLayout()
                .withFullWidth()
                .withStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
                .with(wrapper)
                .withAlign(wrapper, Alignment.MIDDLE_RIGHT);

        return new MVerticalLayout()
                .withMargin(true)
                .withSpacing(true)
                .with(form, buttons);
        
    }

    public void setPage(EducationPage page) {
        form.setPage(page);
    }

}
