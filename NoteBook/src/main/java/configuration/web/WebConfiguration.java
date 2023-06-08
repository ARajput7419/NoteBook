package configuration.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.HashMap;


@Configuration
public class WebConfiguration {

    @Bean("language")
    HashMap<String, String[]> properties() {
        HashMap<String, String[]> properties = new HashMap<>();
        properties.put("cpp", new String[]{"cpp"});
        properties.put("java", new String[]{"java"});
        properties.put("python", new String[]{"py"});
        properties.put("cpp_command", new String[]{"g++"});
        properties.put("java_command", new String[]{"java"});
        properties.put("python_command", new String[]{"python"});
        return properties;

    }

    @Bean
    InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resourceViewResolver = new InternalResourceViewResolver();
        resourceViewResolver.setPrefix("/WEB-INF/views/");
        resourceViewResolver.setSuffix(".jsp");
        return resourceViewResolver;
    }

}
