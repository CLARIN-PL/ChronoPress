package pl.clarin.chronopress.business.security;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebFilter;
import org.apache.shiro.web.servlet.ShiroFilter;

@WebFilter(value = "/*", asyncSupported = true, dispatcherTypes = {DispatcherType.ASYNC, DispatcherType.REQUEST})
public class ShiroFilterActivator extends ShiroFilter {

    private ShiroFilterActivator() {
    }
}
