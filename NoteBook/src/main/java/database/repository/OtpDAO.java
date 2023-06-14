package database.repository;

import database.entity.Otp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class OtpDAO {

    @Autowired
    private EntityManager entityManager;

    public void insert(Otp otp){
        entityManager.persist(otp);
    }

    public Otp get(String email){
        return entityManager.find(Otp.class,email);
    }

    public void update(Otp otp){
        entityManager.refresh(otp);
    }

    public void cleanUp(){
        Query query = entityManager.createQuery("delete from Otp o where o.expire < CURRENT_TIMESTAMP");
        query.executeUpdate();
    }


}
