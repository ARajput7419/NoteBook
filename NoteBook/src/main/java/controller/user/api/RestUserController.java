package controller.user.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class RestUserController {

    @GetMapping("/otp_generate")
    public ResponseEntity<String> generateOtp(@RequestParam("email") String email){



        return null;
    }

}
