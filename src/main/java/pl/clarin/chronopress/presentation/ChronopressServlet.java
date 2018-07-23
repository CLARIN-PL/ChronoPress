package pl.clarin.chronopress.presentation;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import javax.servlet.ServletException;

public class ChronopressServlet extends VaadinServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new SessionInitListener() {

            @Override
            public void sessionInit(SessionInitEvent event) {
                event.getSession().addBootstrapListener(new BootstrapListener() {

                    @Override
                    public void modifyBootstrapFragment(
                            BootstrapFragmentResponse response) {
                    }

                    @Override
                    public void modifyBootstrapPage(BootstrapPageResponse response) {
                        response.getDocument().head().prependElement("meta")
                                .attr("name", "google-site-verification")
                                .attr("content", "i5o7qV6zJ4ng8_h3XTgquNtLtqNJmv_7mxmMv4c3zbI");
                    }
                }
                );
            }
        });
    }
}
