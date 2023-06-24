package configuration.security.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import service.web.ResourceService;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;



public class ResourceAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Value("${resources}")
    private String resourceDirectory;


    @Autowired
    private ResourceService resourceService;


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        String resourceUrl = request.getRequestURI();
        int index = resourceUrl.indexOf(resourceDirectory);
        String location = resourceUrl.substring(index);
        Boolean status = resourceService.authorize(location);
        if(status == null || !status) return new AuthorizationDecision(false);
        return new AuthorizationDecision(true);
    }
}
