package pl.edu.pwr.chrono.webui.ui.main.layout.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.event.NavigationEvent;
import pl.edu.pwr.chrono.webui.infrastructure.event.UIEventBus;
import pl.edu.pwr.chrono.webui.ui.dataanalyse.DataAnalyseView;
import pl.edu.pwr.chrono.webui.ui.home.HomeView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by tnaskret on 04.02.16.
 */


@SpringComponent
@UIScope
public class MainMenu extends HorizontalLayout{

    @Autowired
    private UIEventBus uiEventBus;

    @PostConstruct
    public  void init(){
        setWidth(100,Unit.PERCENTAGE);
        uiEventBus.register(this);

        HorizontalLayout wrapper = new HorizontalLayout();
        Image gear = new Image(null, new ThemeResource("img/menu_graf.png"));
        gear.setHeight(32 ,Unit.PIXELS);

        wrapper.addComponent(gear);

        HorizontalLayout barwrapper = new HorizontalLayout();
        barwrapper.addStyleName(ChronoTheme.MENU_BAR_WRAPPER);
        barwrapper.setMargin(new MarginInfo(false,true,false,true));

        MenuBar barmenu = new MenuBar();
        barmenu.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        barmenu.addStyleName(ChronoTheme.MENU_BAR);

        MenuBar.MenuItem home = barmenu.addItem("Home", FontAwesome.HOME, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                uiEventBus.post( new NavigationEvent(HomeView.VIEW_NAME));
            }
        });

        MenuBar.MenuItem tools = barmenu.addItem("Narzędzia", FontAwesome.WRENCH, null);
        tools.addItem("Szukaj", FontAwesome.SEARCH, null);
        tools.addItem("Przglądaj próbki", FontAwesome.BOOK, null);

        tools.addItem("Analiza danych", FontAwesome.COGS, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                uiEventBus.post( new NavigationEvent(DataAnalyseView.VIEW_NAME));
            }
        });

        MenuBar.MenuItem education = barmenu.addItem("Edukacja", FontAwesome.GRADUATION_CAP, null);

        MenuBar.MenuItem contact = barmenu.addItem("Kontakt", FontAwesome.ENVELOPE_O, null);

        barwrapper.addComponent(barmenu);
        wrapper.addComponent(barwrapper);
        addComponent(wrapper);
        setComponentAlignment(wrapper, Alignment.BOTTOM_RIGHT);
    }

    @PreDestroy
    public  void preDestroy(){
        uiEventBus.unregister(this);
    }
}
