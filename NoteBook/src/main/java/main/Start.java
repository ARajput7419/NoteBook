package main;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@EnableScheduling
@SpringBootApplication
@EnableWebMvc
@EntityScan(basePackages = "database")
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"configuration","controller","responses","beans","database","service"})
public class Start implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:/home/aniket/IdeaProjects/NoteBook/NoteBook/src/main/webapp/WEB-INF/static/");
    }

    public static void main(String[] args) {
        SpringApplication.run(Start.class,args);
    }

    @Value("${page_size}")
    private int page_size;


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        Hibernate5Module module = new Hibernate5Module();
        module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);

        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modulesToInstall(module);
        builder.simpleDateFormat("yyyy/MM/dd HH:mm:ss");

        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));

    }



    @Bean
    public CommandLineRunner commandLineRunner(){
        return (args)->{

            if(page_size<=0){
                throw new Exception("Invalid Page Size Value");
            }
        };
    }

}
