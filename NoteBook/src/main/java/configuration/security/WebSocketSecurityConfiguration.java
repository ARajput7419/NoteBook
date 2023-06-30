package configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.model.SpELContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletRequest;



class CustomFunction{

    public boolean check(HttpServletRequest request , Authentication authentication){

        return false;
    }

}


@Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {



    @Bean
    CustomFunction customFunction(){
        StandardEvaluationContext context;
        Expression expression;
        ExpressionParser parser;
        return new CustomFunction();
    }



    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpSubscribeDestMatchers("/")
                .access("@customFunction.check(#request,authentication)");

    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {

    }
}
