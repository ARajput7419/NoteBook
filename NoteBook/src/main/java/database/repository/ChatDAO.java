package database.repository;

import database.entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
public class ChatDAO {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public Chat get(int id){
        return entityManager.find(Chat.class,id);
    }

    @Transactional
    public void delete(int id){
        Chat chat = get(id);
        if(chat == null) return;
        entityManager.remove(chat);
    }

    @Transactional
    public void insert(Chat chat){
        entityManager.persist(chat);
    }

    @Transactional
    public void update(Chat chat){
        entityManager.refresh(chat);
    }
}
