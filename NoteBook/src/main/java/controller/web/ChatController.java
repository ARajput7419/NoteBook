package controller.web;

import database.entity.Chat;
import database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.web.ChatService;
import java.util.*;


@RequestMapping("/chat")
@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;


    private boolean checkCurrentUser(String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUser = (String) authentication.getPrincipal();
        return authenticatedUser.equals(username);
    }

    @GetMapping("/")
    public String chat(Model model){
        List<Chat> chats = chatService.chatsByUser();
        LinkedHashMap<String,ArrayList<Chat>> linkedHashMap = new LinkedHashMap<>();
        for (Chat chat : chats){
            String from_user = chat.getFrom().getEmail();
            String to_user = chat.getTo().getEmail();
            if (!checkCurrentUser(from_user))
            {
                linkedHashMap.putIfAbsent(from_user,new ArrayList<>());
                linkedHashMap.get(from_user).add(chat);
            }
            if (!checkCurrentUser(to_user))
            {
                linkedHashMap.putIfAbsent(to_user,new ArrayList<>());
                linkedHashMap.get(to_user).add(chat);
            }

        }
        model.addAttribute("chats",linkedHashMap);
        return "chat";
    }

}
