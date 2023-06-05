package database.repository;


import database.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
public class ResourceDAO {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public Resource get(int id){
        return entityManager.find(Resource.class,id);
    }

    @Transactional
    public void delete(int id){
        Resource resource = get(id);
        if(resource == null) return;
        entityManager.remove(resource);
    }

    @Transactional
    public void insert(Resource resource){
        entityManager.persist(resource);
    }

    @Transactional
    public void update(Resource resource){
        entityManager.refresh(resource);
    }


}
