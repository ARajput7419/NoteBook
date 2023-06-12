package main;

import database.entity.User;
import database.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import service.web.UserService;

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



    @Bean
    public CommandLineRunner commandLineRunner(){
        return (args)->{

            if(page_size<=0){
                throw new Exception("Invalid Page Size Value");
            }
        };
    }

}
