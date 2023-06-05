package configuration;

import beans.DirectoryHandler;
import controller.API;
import main.Start;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AOPConfiguration {

    @Autowired
    private DirectoryHandler directoryHandler;

    @Value("#{(servletContext.getRealPath('/'))+'${destination}'}")
    private String apiDirectory;

    @Value("#{(servletContext.getRealPath('/'))+'${resources}'}")
    private String resourcesDirectory;

    @Before("execution(* controller.API.*(..) )")
    private void setCmd(){
        directoryHandler.setCwd(apiDirectory);
    }
    @Before("execution(* controller.resources.api.ResourcesController.*(..))")
    private void setResourcesCwd(){
        directoryHandler.setCwd(resourcesDirectory);
    }

    @After("execution(* controller.API.*(..))")
    private void cleanUp(JoinPoint joinPoint){

        Object objects [] = joinPoint.getArgs();

        HttpServletRequest request = null;

        for (Object object : objects){

            if (object instanceof HttpServletRequest){

                request = (HttpServletRequest) object;
                break;
            }
        }

        if (request == null) return;

        String ip = request.getRemoteAddr().replace(":","_").replace(".","_");

        directoryHandler.deleteDirectory(ip);

    }

}
