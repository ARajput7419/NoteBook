package controller.user.api;


import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.web.UserService;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/user")
public class RestUserController {

    @Autowired
    private UserService userService;

    @ExceptionHandler
    public ResponseEntity<String> exceptionHandler(Exception e){
        return new ResponseEntity<>("Internal Server Error ",HttpStatus.OK);
    }


    @GetMapping("/otp_generate")
    public ResponseEntity<String> generateOtp(@RequestParam("email") String email) throws MessagingException {
        userService.generateOtp(email);
        return new ResponseEntity<>("Otp Sent Successfully", HttpStatus.OK);
    }

    @GetMapping("/exists")
    public ResponseEntity<String> exists(@RequestParam("email") String email){
        if (email == null || email.length() == 0) return new ResponseEntity<>("Bad Request",HttpStatus.BAD_REQUEST);
        else {
            UserService.UserExistenceStatus status = userService.exists(email);
            if (status != UserService.UserExistenceStatus.NOT_EXIST) return new ResponseEntity<>("User Exists",HttpStatus.OK);
            else return new ResponseEntity<>("User Does Not Exists",HttpStatus.NOT_FOUND);

        }

    }

}
