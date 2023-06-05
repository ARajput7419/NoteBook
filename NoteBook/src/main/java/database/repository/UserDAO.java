package database.repository;


import database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
public class UserDAO {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public User get(String email){
        return entityManager.find(User.class,email);
    }

    @Transactional
    public void delete(String email){
        User user = get(email);
        if(user == null) return;
        entityManager.remove(user);
    }

    @Transactional
    public void insert(User user){
        entityManager.persist(user);
    }

    @Transactional
    public void update(User user){
        entityManager.refresh(user);
    }

}
