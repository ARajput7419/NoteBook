package configuration.security;

import configuration.security.authentication.DatabaseAuthenticationProvider;
import configuration.security.authentication.DatabaseUserDetailService;
import configuration.security.authorization.ResourceAuthorizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import service.web.UserService;

import java.util.List;

@Configuration
public class SecurityConfiguration {


    @Value("${google-clientID}")
    private String googleClientId;

    @Value("${google-secret}")
    private String googleSecret;

    @Value("${github-clientID}")
    private String githubClientId;

    @Value("${github-secret}")
    private String githubSecret;

    @Autowired
    private DatabaseAuthenticationProvider authenticationProvider;

    @Autowired
    private UserService userService;

    @Value("${resources}")
    private String resources;



    @Value("${redirect_uri}")
    private String redirect_uri;


     private List<ClientRegistration> registration(){

        return List.of(CommonOAuth2Provider.GOOGLE.getBuilder("google").clientId(googleClientId).clientSecret(googleSecret).redirectUri(redirect_uri).build());

    }



    private ClientRegistrationRepository registrationRepository (){

        return new InMemoryClientRegistrationRepository(registration());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {


        OAuth2LoginAuthenticationFilter f;

        httpSecurity.oauth2Login().
                clientRegistrationRepository(registrationRepository())
                        .successHandler(((request, response, authentication) -> {
                            userService.loginThroughOauth2(request,response,authentication);
                            savedRequestAwareAuthenticationSuccessHandler().onAuthenticationSuccess(request,
                                    response,authentication);
                        }))
                .loginPage("/user/login");

        httpSecurity.formLogin()
                        .loginPage("/user/login")
                                .loginProcessingUrl("/login_processing")
                                        .failureForwardUrl("/user/login/failure")
                                                .defaultSuccessUrl("/",true)
                                                        .usernameParameter("username")
                                                                .passwordParameter("password");

        httpSecurity.httpBasic();

        httpSecurity.logout();


        httpSecurity.authorizeHttpRequests((customizer)->{
            customizer.mvcMatchers("/execution/api/**").permitAll();
            customizer.mvcMatchers("/login_processing").permitAll();
            customizer.mvcMatchers("/chat/").authenticated();
            customizer.mvcMatchers("/notes/create").authenticated();
            customizer.mvcMatchers("/notes/edit").authenticated();
            customizer.mvcMatchers("/notes/submit").authenticated();
            customizer.mvcMatchers("/notes/public/**").permitAll();
            customizer.mvcMatchers("/notes/view/{id}").permitAll();
            customizer.mvcMatchers("/").permitAll();
            customizer.mvcMatchers("/user/**").permitAll();
            customizer.mvcMatchers("/api/user/**").permitAll();
            customizer.mvcMatchers("/api/resources/**").permitAll();
            customizer.mvcMatchers("/api/notes/public/**").permitAll();
            customizer.mvcMatchers("/api/notes/private/**").authenticated();
            customizer.mvcMatchers("/api/notes/{id}").authenticated();
            customizer.mvcMatchers("/"+resources+"/**").access(resourceAuthorizationManager());
        });

        httpSecurity.authenticationProvider(authenticationProvider);

        httpSecurity.csrf();

        return httpSecurity.build();

     }

     @Bean
     public ResourceAuthorizationManager resourceAuthorizationManager(){
         return new ResourceAuthorizationManager();
     }



     @Bean
     public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler(){
         return new SavedRequestAwareAuthenticationSuccessHandler();
     }

     @Bean
     public DatabaseAuthenticationProvider authenticationProvider(){
         return new DatabaseAuthenticationProvider();
     }

    @Bean
    public UserDetailsService detailsService(){
        return new DatabaseUserDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
