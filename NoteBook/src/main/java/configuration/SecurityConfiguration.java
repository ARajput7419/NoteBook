package configuration;

import configuration.authentication.DatabaseAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

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


     private List<ClientRegistration> registration(){

        return List.of(CommonOAuth2Provider.GOOGLE.getBuilder("google").clientId(googleClientId).clientSecret(googleSecret).build()
                , CommonOAuth2Provider.GITHUB.getBuilder("github").clientId(githubClientId).clientSecret(githubSecret).build());

    }



    private ClientRegistrationRepository registrationRepository (){
        return new InMemoryClientRegistrationRepository(registration());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.oauth2Login().
                clientRegistrationRepository(registrationRepository())
                        .successHandler(((request, response, authentication) -> {

                            // my code

                            savedRequestAwareAuthenticationSuccessHandler().onAuthenticationSuccess(request,
                                    response,authentication);
                        }));

        httpSecurity.logout();

        httpSecurity.authorizeHttpRequests((customizer)->{
            customizer.mvcMatchers("/api/**").permitAll();
            customizer.mvcMatchers("/**").authenticated();
        });

        httpSecurity.authenticationProvider(authenticationProvider);

        httpSecurity.csrf();

        return httpSecurity.build();

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
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        UserDetails user1 = User.withUsername("aniket")
                .password(passwordEncoder().encode("rana"))
                .roles("admin")
                .build();
        detailsManager.createUser(user1);
        return detailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
