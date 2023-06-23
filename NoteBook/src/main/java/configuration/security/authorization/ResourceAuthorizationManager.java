package configuration.security.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;


public class ResourceAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Value("resources")
    private String resourceDirectory;




    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return null;
    }
}
