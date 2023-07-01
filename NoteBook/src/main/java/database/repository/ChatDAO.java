package database.repository;

import database.entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChatDAO {

    @Autowired
    private  EntityManager entityManager;


    public Chat get(int id){
        return entityManager.find(Chat.class,id);
    }

    public void delete(int id){
        Chat chat = get(id);
        if(chat == null) return;
        entityManager.remove(chat);
    }


    public void insert(Chat chat){
        entityManager.persist(chat);
    }


    public void update(Chat chat){

    }

    public List<Chat> chatsByUser(String username){
        TypedQuery<Chat> chats = entityManager.createQuery("select c from Chat c  join c.from f join c.to t where f.email = :username or t.email = :username order by c.timestamp desc",Chat.class);
        chats.setParameter("username",username);
        return chats.getResultList();
    }

}
