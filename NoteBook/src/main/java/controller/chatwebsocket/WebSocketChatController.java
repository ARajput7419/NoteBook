package controller.chatwebsocket;
import database.entity.Chat;
import database.entity.Message;
import database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import service.web.ChatService;
import service.web.UserService;
import java.sql.Timestamp;

@Controller
public class WebSocketChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    public Message received(@Payload Message message){
        System.out.println(message);
        System.out.println(message.getMessage());
        Chat chat = new Chat();
        chat.setTimestamp(new Timestamp(System.currentTimeMillis()));
        chat.setMessage(message.getMessage());
        User sender = userService.getByUsername(message.getSender());
        User receiver = userService.getByUsername(message.getReceiver());
        if (sender == null || receiver == null || !message.getSender().equals((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            return null;
        }
        chat.setFrom(sender);
        chat.setTo(receiver);
        chatService.insert(chat);
        messagingTemplate.convertAndSendToUser(message.getReceiver(),"/private",message);
        return message;
    }


}
