package database.repository;


import database.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class NoteDAO {

    @Autowired
    EntityManager entityManager;


    public Note get(int id){
        return entityManager.find(Note.class,id);
    }


    public void delete(int id){
        Note note = get(id);
        if(note == null) return;
        entityManager.remove(note);
    }


    public List<Note> searchByKeywordPublic(String keyword,int count,int offset){
        TypedQuery<Note> query = entityManager.createQuery("select n from Note n where n.name like :key and visibility = 'Public'",Note.class);
        query.setParameter("key","%"+keyword+"%");
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }


    public List<Note> searchByKeywordPrivate(String keyword,String username,int count,int offset){
        TypedQuery<Note> query = entityManager.createQuery("select n from Note n join n.user u where n.name like :key and u.email = :username ",Note.class);
        query.setParameter("key","%"+keyword+"%");
        query.setParameter("username",username);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }


    public int searchByKeywordPublicCount(String keyword){
        TypedQuery<Long> query = entityManager.createQuery("select count(n) from Note n where n.name like :key and visibility = 'Public'",Long.class);
        query.setParameter("key","%"+keyword+"%");
        return (int)(long)(query.getSingleResult());
    }


    public int searchByKeywordPrivateCount(String keyword,String username){
        TypedQuery<Long> query = entityManager.createQuery("select count(n) from Note n join n.user u where n.name like :key and u.email = :username",Long.class);
        query.setParameter("key","%"+keyword+"%");
        query.setParameter("username",username);
        return (int)(long)(query.getSingleResult());
    }


    public List<Note> getNotesPrivate(String username,int count,int offset){

        TypedQuery<Note> query = entityManager.createQuery("Select n from Note n join n.user u where u.email = :username ",Note.class);
        query.setParameter("username",username);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();

    }


    public int getNotesPrivateCount(String username){
        TypedQuery<Long> query = entityManager.createQuery("Select count(n) from Note n join n.user u  where u.email = :username ",Long.class);
        query.setParameter("username",username);
        return (int)(long)query.getSingleResult();
    }


    public List<Note> getNotesPublic(int count,int offset){

        TypedQuery<Note> query = entityManager.createQuery("Select n from Note n  where n.visibility = 'Public'",Note.class);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();

    }


    public int getNotesPublicCount(){
        TypedQuery<Long> query = entityManager.createQuery("Select count(n) from Note n  where visibility = 'Public' ",Long.class);
        return (int)(long)(query.getSingleResult());
    }



    public void insert(Note note){
        entityManager.persist(note);
    }


    public void update(Note note){

    }

    public List<Note> getRecentPublicNotes(int count){
        TypedQuery<Note> query = entityManager.createQuery("select n from Note n where n.visibility = 'Public' order by n.timestamp desc",Note.class);
        query.setMaxResults(count);
        return query.getResultList();
    }


}
