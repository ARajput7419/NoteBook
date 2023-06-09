package database.repository;


import database.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class NoteDAO {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public Note get(int id){
        return entityManager.find(Note.class,id);
    }

    @Transactional
    public void delete(int id){
        Note note = get(id);
        if(note == null) return;
        entityManager.remove(note);
    }

    @Transactional
    public List<Note> searchByKeywordPublic(String keyword,int count,int offset){
        TypedQuery<Note> query = entityManager.createQuery("select n from Note n where n.name like :key and visibility = 'Public' limit :count offset :offset",Note.class);
        query.setParameter("key","%"+keyword+"%");
        query.setParameter("count",count);
        query.setParameter("offset",offset);
        return query.getResultList();
    }

    @Transactional
    public List<Note> searchByKeywordPrivate(String keyword,String username,int count,int offset){
        TypedQuery<Note> query = entityManager.createQuery("select n from Note n join n.user u where n.name like :key and u.email = :username limit :count  offset :offset",Note.class);
        query.setParameter("key","%"+keyword+"%");
        query.setParameter("username",username);
        query.setParameter("count",count);
        query.setParameter("offset",offset);
        return query.getResultList();
    }

    @Transactional
    public List<Note> getNotesPrivate(String username,int count,int offset){

        TypedQuery<Note> query = entityManager.createQuery("Select n from Note n join n.user u where u.email = :username limit :count offset :offset ",Note.class);
        query.setParameter("username",username);
        query.setParameter("count",count);
        query.setParameter("offset",offset);
        return query.getResultList();

    }

    @Transactional
    public int getNotesPrivateCount(String username){
        TypedQuery<Integer> query = entityManager.createQuery("Select count(*) from Note n  where u.email = :username ",Integer.class);
        query.setParameter("username",username);
        return query.getSingleResult();
    }

    @Transactional
    public List<Note> getNotesPublic(int count,int offset){

        TypedQuery<Note> query = entityManager.createQuery("Select n from Note n  where n.visibility = 'Public' limit :count offset :offset ",Note.class);
        query.setParameter("count",count);
        query.setParameter("offset",offset);
        return query.getResultList();

    }

    @Transactional
    public int getNotesPublicCount(){
        TypedQuery<Integer> query = entityManager.createQuery("Select count(*) from Note n  where visibility = 'Public' ",Integer.class);
        return query.getSingleResult();
    }


    @Transactional
    public void insert(Note note){
        entityManager.persist(note);
    }

    @Transactional
    public void update(Note note){
        entityManager.refresh(note);
    }


}
