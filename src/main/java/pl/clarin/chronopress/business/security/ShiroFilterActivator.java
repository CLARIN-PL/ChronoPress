package pl.clarin.chronopress.business.security;

import javax.servlet.annotation.WebFilter;
import org.apache.shiro.web.servlet.ShiroFilter;

@WebFilter(value = "/*", asyncSupported = true)
public class ShiroFilterActivator extends ShiroFilter {

    private ShiroFilterActivator() {
    }
}
