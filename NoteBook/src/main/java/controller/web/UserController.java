package controller.web;

import database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.web.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,Model model){
        boolean status =userService.exists(user.getUsername());
        if (!status){
            try {
                userService.registerUser(user);
                model.addAttribute("login",false);
                return "home";
            }
            catch (Exception e){
                model.addAttribute("message","Registration Failed");
                return "register";
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
