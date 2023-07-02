package configuration.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletRequest;



@Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {



//    class CustomFunction{
//
//        public boolean check(HttpServletRequest request , Authentication authentication){
//             String requestUri = request.getRequestURI();
//             if(requestUri.startsWith("/chat/user/"+(String)authentication.getPrincipal())){
//                 return true;
//             } else return false;
//        }
//
//    }
//
//
//    @Bean
//    CustomFunction customFunction(){
//        return new CustomFunction();
//    }
//
//
//
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages.simpSubscribeDestMatchers("/chat/user/**")
//                .access("@customFunction.check(#request,authentication)");
//
//    }

}
