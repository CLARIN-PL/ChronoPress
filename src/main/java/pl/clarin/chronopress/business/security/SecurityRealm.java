package pl.clarin.chronopress.business.security;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import pl.clarin.chronopress.business.user.boundary.UserFacade;
import pl.clarin.chronopress.business.user.entity.User;

public class SecurityRealm extends AuthorizingRealm {

    private static final Logger logger = Logger.getLogger(SecurityRealm.class.getName());

    private UserFacade facade;

    public SecurityRealm() {
        super();

        setAuthenticationCachingEnabled(Boolean.TRUE);

        try {
            InitialContext ctx = new InitialContext();
            String moduleName = (String) ctx.lookup("java:module/ModuleName");
            this.facade = (UserFacade) ctx.lookup(String.format("java:global/%s/UserFacade", moduleName));
        } catch (NamingException ex) {
            logger.log(Level.WARNING, "Cannot do the JNDI Lookup to instantiate the User service : {0}", ex);
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
        String username = userPassToken.getUsername();

        if (username == null) {
            logger.warning("Username is null.");
            return null;
        }

        User user = this.facade.findUserByUsername(username);

        if (user == null) {
            logger.log(Level.WARNING, "No account found for user [{0}]", username);
            throw new IncorrectCredentialsException();
        }

        return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principals);

        Set<String> roleNames = new HashSet<>();
        roleNames.add(this.facade.findUserByUsername(username).getRole().name().toLowerCase());

        AuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);

        return info;
    }

}
