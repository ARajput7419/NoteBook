package database.repository;


import database.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ResourceDAO {

    @Autowired
    EntityManager entityManager;


    public Resource get(int id){
        return entityManager.find(Resource.class,id);
    }


    public void delete(int id){
        Resource resource = get(id);
        if(resource == null) return;
        entityManager.remove(resource);
    }


    public void insert(Resource resource){
        entityManager.persist(resource);
    }


    public void update(Resource resource){
        entityManager.refresh(resource);
    }


    public  int getResourcesPrivateCount(String username){
        TypedQuery<Integer> query = entityManager.createQuery("select count(r) from Resource r join r.user u where u.email = :username",Integer.class);
        query.setParameter("username",username);
        return query.getSingleResult();
    }


    public List<Resource> getResourcesPrivate(String username , int count , int offset){
        TypedQuery<Resource> query = entityManager.createQuery("select r from Resource r join r.user u where u.email= :username limit :count offset :offset",Resource.class);
        query.setParameter("username",username);
        query.setParameter("count",count);
        query.setParameter("offset",offset);
        return query.getResultList();
    }


    public  int searchByKeywordPrivateCount(String username,String keyword){
        TypedQuery<Integer> query = entityManager.createQuery("select count(r) from Resource r join r.user u where u.email = :username and r.name like :keyword",Integer.class);
        query.setParameter("username",username);
        query.setParameter("keyword","%"+keyword+"%");
        return query.getSingleResult();
    }


    public List<Resource> searchByKeywordPrivate(String username , String keyword,int count , int offset){
        TypedQuery<Resource> query = entityManager.createQuery("select r from Resource r join r.user u where u.email= :username and r.name like :keyword limit :count offset :offset",Resource.class);
        query.setParameter("username",username);
        query.setParameter("keyword",keyword);
        query.setParameter("count",count);
        query.setParameter("offset",offset);
        return query.getResultList();
    }


    public boolean checkIfAlreadyExists(String location){
        TypedQuery<Integer> query = entityManager.createQuery("select count(r) from Resource r where r.location = :location",Integer.class);
        query.setParameter("location",location);
        return query.getSingleResult()==1;
    }

    public void increment(List<String> locations){
        Query query = entityManager.createQuery("update Resource r set r.count = r.count + 1 where r.location = :location ");
        for (String location:locations){
            query.setParameter("location",location);
            query.executeUpdate();
        }

    }

    public void decrement(List<String> locations){
        Query query = entityManager.createQuery("update Resource r set r.count = r.count - 1 where r.location = :location ");
        for (String location:locations){
            query.setParameter("location",location);
            query.executeUpdate();
        }

    }



}
