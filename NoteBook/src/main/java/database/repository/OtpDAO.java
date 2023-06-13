package database.repository;

import database.entity.Otp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;

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


}
