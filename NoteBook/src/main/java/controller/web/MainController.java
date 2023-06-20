package controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.web.NoteService;

@Controller
@RequestMapping("/")
public class MainController {


    @Autowired
    private NoteService noteService;

    @GetMapping("/")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("login",authentication.getClass() != AnonymousAuthenticationToken.class);
        model.addAttribute("notes",noteService.getRecentPublicNotes(10));
        return "home";
    }

}
