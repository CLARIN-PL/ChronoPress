package pl.clarin.chronopress.business.security;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;

@WebListener
public class ShiroListener extends EnvironmentLoaderListener {

    @Inject
    WebSecurityManager securityManager;

    @Inject
    FilterChainResolver filterChainResolver;

    @Override
    protected WebEnvironment createEnvironment(ServletContext sc) {
        DefaultWebEnvironment webEnvironment = (DefaultWebEnvironment) super.createEnvironment(sc);

        webEnvironment.setSecurityManager(securityManager);
        webEnvironment.setFilterChainResolver(filterChainResolver);

        return webEnvironment;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setInitParameter(ENVIRONMENT_CLASS_PARAM, DefaultWebEnvironment.class.getName());
        super.contextInitialized(sce);
    }
}
