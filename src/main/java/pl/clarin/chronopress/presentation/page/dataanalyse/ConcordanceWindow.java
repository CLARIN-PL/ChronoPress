package pl.clarin.chronopress.presentation.page.dataanalyse;

import com.vaadin.ui.*;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.page.dataanalyse.result.ConcordanceList;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class ConcordanceWindow extends Window {

    @Inject
    DbPropertiesProvider provider;

    VerticalLayout content;

    @PostConstruct
    public void init() {
        setCaption(provider.getProperty("window.concordance"));
        addStyleName(ChronoTheme.WINDOW);
        setWidth(90, Unit.PERCENTAGE);
        setHeight(90, Unit.PERCENTAGE);
        setModal(true);
        content = new MVerticalLayout()
                .withSize(MSize.FULL_SIZE);
        setContent(content);
        center();
    }

    public void setConcordance(ConcordanceList concordanceList) {
        content.removeAllComponents();
        content.addComponent(concordanceList.showResult());
    }

}
