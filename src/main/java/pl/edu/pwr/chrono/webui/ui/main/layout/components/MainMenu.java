package pl.edu.pwr.chrono.webui.ui.main.layout.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.chrono.webui.infrastructure.event.NavigationEvent;
import pl.edu.pwr.chrono.webui.infrastructure.event.UIEventBus;
import pl.edu.pwr.chrono.webui.ui.admin.LoginWindow;
import pl.edu.pwr.chrono.webui.ui.dataanalyse.DataAnalysisView;
import pl.edu.pwr.chrono.webui.ui.education.EducationView;
import pl.edu.pwr.chrono.webui.ui.home.HomeView;
import pl.edu.pwr.chrono.webui.ui.samplebrowser.SampleBrowserView;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringComponent
@UIScope
public class MainMenu extends HorizontalLayout{

    @Autowired
    private UIEventBus uiEventBus;

    @Autowired
    private DbPropertiesProvider provider;

    @Autowired
    private LoginWindow loginWindow;

    @Autowired
    private AuthenticationManager auth;


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

        MenuBar barmenu = buildMenuBar();

        barwrapper.addComponent(barmenu);
        wrapper.addComponent(barwrapper);
        addComponent(wrapper);
        setComponentAlignment(wrapper, Alignment.BOTTOM_RIGHT);

        initSignIn();
    }

    private void initSignIn() {
        loginWindow.getSignIn().addClickListener(event -> {
            try {
                UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(
                        loginWindow.getUsername(), loginWindow.getPassword());
                Authentication result = auth.authenticate(request);
                SecurityContextHolder.getContext().setAuthentication(result);

            } catch (AuthenticationException e) {
                Notification.show("Login failed.", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            loginWindow.close();
        });
    }


    private MenuBar buildMenuBar() {

        MenuBar barmenu = new MenuBar();
        barmenu.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        barmenu.addStyleName(ChronoTheme.MENU_BAR);

        MenuBar.MenuItem home = barmenu.addItem(provider.getProperty("menu.home"), FontAwesome.HOME,
                (MenuBar.Command) selectedItem -> uiEventBus.post(new NavigationEvent(HomeView.VIEW_NAME)));

        MenuBar.MenuItem tools = barmenu.addItem(provider.getProperty("menu.tools"), FontAwesome.WRENCH, null);
        tools.addItem(provider.getProperty("menu.search"), FontAwesome.SEARCH, null);
        tools.addItem(provider.getProperty("menu.sample.viewer"), FontAwesome.BOOK,
                (MenuBar.Command) selectedItem -> uiEventBus.post(new NavigationEvent(SampleBrowserView.VIEW_NAME)));

        tools.addItem(provider.getProperty("menu.data.analyse"), FontAwesome.COGS,
                (MenuBar.Command) selectedItem -> uiEventBus.post(new NavigationEvent(DataAnalysisView.VIEW_NAME)));

        MenuBar.MenuItem education = barmenu.addItem(provider.getProperty("menu.education"), FontAwesome.GRADUATION_CAP,
                (MenuBar.Command) selectedItem -> uiEventBus.post(new NavigationEvent(EducationView.VIEW_NAME)));

        MenuBar.MenuItem contact = barmenu.addItem(provider.getProperty("menu.contact"), FontAwesome.ENVELOPE_O, null);

        MenuBar.MenuItem signin = barmenu.addItem(provider.getProperty("menu.sign.in"), FontAwesome.SIGN_IN,
                (MenuBar.Command) selectedItem -> UI.getCurrent().addWindow(loginWindow));
        return barmenu;
    }

    @PreDestroy
    public  void preDestroy(){
        uiEventBus.unregister(this);
    }
}
