package main.scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service.web.ResourceService;
import service.web.UserService;

@Component
public class Scheduler{

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Scheduled(fixedRate = 18000000)
    public void cleanUp(){
        resourcesCleanUp();
        otpCleanUp();
    }

    public void resourcesCleanUp(){
        resourceService.cleanUp();
    }

    public void otpCleanUp(){
        userService.otpCleanUp();
    }

}
