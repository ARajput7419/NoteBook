package database.repository;


import database.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
    public void insert(Note note){
        entityManager.persist(note);
    }

    @Transactional
    public void update(Note note){
        entityManager.refresh(note);
    }


}
