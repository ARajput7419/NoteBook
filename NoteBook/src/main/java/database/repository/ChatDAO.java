package database.repository;

import database.entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;

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
}
