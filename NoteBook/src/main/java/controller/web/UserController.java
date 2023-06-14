package controller.web;

import database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.web.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model, @RequestParam("otp") String otp){

        UserService.UserExistenceStatus status =userService.exists(user.getUsername());
        if (status == UserService.UserExistenceStatus.NOT_EXIST){

            UserService.OtpStatus otpStatus = userService.verifyOtp(user.getEmail(),otp);

            if (otpStatus == UserService.OtpStatus.EXPIRED) {
                model.addAttribute("message","Otp is Expired");
                return "register";
            }
            else if (otpStatus == UserService.OtpStatus.FAILED){
                model.addAttribute("message","Otp Verification is Failed");
                return "register";
            }
            else{
                try {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userService.registerUser(user);
                    model.addAttribute("login",false);
                    return "home";
                }
                catch (Exception e){
                    model.addAttribute("message","Registration Failed");
                    return "register";
                }
            }
        }
        else if (UserService.UserExistenceStatus.Oauth2 == status){

            UserService.OtpStatus otpStatus = userService.verifyOtp(user.getEmail(),otp);

            if (otpStatus == UserService.OtpStatus.EXPIRED) {
                model.addAttribute("message","Otp is Expired");
                return "register";
            }
            else if (otpStatus == UserService.OtpStatus.FAILED){
                model.addAttribute("message","Otp Verification is Failed");
                return "register";
            }
            else{
                try {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setOauth2(false);
                    userService.update(user);
                    model.addAttribute("login",false);
                    return "home";
                }
                catch (Exception e){
                    model.addAttribute("message","Registration Failed");
                    return "register";
                }
            }
        }
        else{
            model.addAttribute("message","User Already Exists");
            return "register";
        }
    }

    @GetMapping("/login/failure")
    public String error(Model model){
        model.addAttribute("message","Login Failed");
        return "login";
    }


}
