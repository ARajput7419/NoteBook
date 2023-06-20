package database.repository;


import database.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
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

    }


    public  int getResourcesPrivateCount(String username){
        TypedQuery<Long> query = entityManager.createQuery("select count(r) from Resource r join r.user u where u.email = :username",Long.class);
        query.setParameter("username",username);
        return (int)(long)query.getSingleResult();
    }


    public List<Resource> getResourcesPrivate(String username , int count , int offset){
        TypedQuery<Resource> query = entityManager.createQuery("select r from Resource r join r.user u where u.email= :username ",Resource.class);
        query.setParameter("username",username);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }


    public  int searchByKeywordPrivateCount(String username,String keyword){
        TypedQuery<Long> query = entityManager.createQuery("select count(r) from Resource r join r.user u where u.email = :username and r.name like :keyword",Long.class);
        query.setParameter("username",username);
        query.setParameter("keyword","%"+keyword+"%");
        return (int)(long)query.getSingleResult();
    }


    public List<Resource> searchByKeywordPrivate(String username , String keyword,int count , int offset){
        TypedQuery<Resource> query = entityManager.createQuery("select r from Resource r join r.user u where u.email= :username and r.name like :keyword ",Resource.class);
        query.setParameter("username",username);
        query.setParameter("keyword",keyword);
        query.setMaxResults(count);
        query.setFirstResult(offset);
        return query.getResultList();
    }


    public boolean checkIfAlreadyExists(String location){
        TypedQuery<Long> query = entityManager.createQuery("select count(r) from Resource r where r.location = :location",Long.class);
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

    public List<String> getLocations(){
        TypedQuery<Resource> query = entityManager.createQuery("select r from Resource r where r.count = 0 ",Resource.class);
        List<String> locations = new ArrayList<>();
        for(Resource resource : query.getResultList()){
            locations.add(resource.getLocation());
        }
        return locations;
    }

    public void cleanUp(){
        Query query = entityManager.createQuery("delete from Resource r where r.count = 0 ");
        query.executeUpdate();
    }



}
