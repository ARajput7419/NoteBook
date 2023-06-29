package service.web;

import database.entity.Chat;
import database.repository.ChatDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.transaction.Transactional;
import java.util.List;

public class ChatService {

    @Autowired
    private ChatDAO chatDAO;

    @Transactional
    public void store(Chat chat){
        chatDAO.insert(chat);
    }

    @Transactional
    public String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

    @Transactional
    public List<Chat> chatsByUser(){
        return chatDAO.chatsByUser(getUsername());
    }

}
