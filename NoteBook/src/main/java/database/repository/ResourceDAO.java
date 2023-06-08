package database.repository;


import database.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.GenerationType;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

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

    @Transactional
    public  int totalResources(String username){
        TypedQuery<Integer> query = entityManager.createQuery("select count(*) from Resource r join r.user u where u.email = :username",Integer.class);
        query.setParameter("username",username);
        return query.getSingleResult();
    }

    @Transactional
    public List<Resource> getAll(String username , int count , int offset){
        TypedQuery<Resource> query = entityManager.createQuery("select r from Resource r join r.user u where u.email= :username limit :count offset :offset",Resource.class);
        query.setParameter("username",username);
        query.setParameter("count",count);
        query.setParameter("offset",offset);
        return query.getResultList();
    }


}
