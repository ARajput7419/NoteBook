package controller;

import org.apache.catalina.servlets.DefaultServlet;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

@Controller
public class Test {


    @GetMapping("/aniket")
    @ResponseBody
    public String aniket(){
        return "ANIKET";
    }
    @GetMapping("/mylogin")
    public String mylogin(){

        return "login";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String name(HttpServletRequest request){
        SimpleDateFormat format;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//        return "<img src=\""+token.getPrincipal().getAttribute("picture")+"\">";

        UsernamePasswordAuthenticationFilter filter;
        CsrfFilter webl;
        DefaultLoginPageGeneratingFilter f;

        OAuth2AuthorizationRequestRedirectFilter f1;

        LogoutConfigurer configurer;

        AnonymousAuthenticationFilter f6;

        return "Okay";
    }

    @GetMapping("/hello1")
    @ResponseBody
    public String name1(HttpServletRequest request){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//        return "<img src=\""+token.getPrincipal().getAttribute("picture")+"\">";

        return "Hoooreyyy";
    }

}
