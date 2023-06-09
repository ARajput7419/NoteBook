package service.web;

import database.entity.Note;
import database.repository.NoteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {

     @Autowired
     private NoteDAO noteDAO;

     public List<Note> searchNotes(String keyword){
         return noteDAO.searchByKeyword(keyword);
     }

     public List<Note> privateSearch(String keyword){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = (String) authentication.getPrincipal();
         return noteDAO.searchByKeywordPrivate(keyword,username);
     }

    public List<Note> getAll(int count,int offset){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return noteDAO.getAll(username,count,offset);
    }

    public int totalNotes(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return noteDAO.totalNotes(username);
    }


    public List<Note> getAllPublic(int count,int offset){
        return noteDAO.getAllPublic(count,offset);
    }

    public int totalPublicNotes(){
        return noteDAO.totalPublicNotes();
    }

     public Note getNote(int id) throws Exception {
         Note note = noteDAO.get(id);
         if (note == null) return null;
         if (note.getVisibility().equals("Public")) return note;
         Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null) throw new Exception("Note Authenticated");
         String username = (String) authentication.getPrincipal();
         if (note.getUser().getEmail().equals(username)){
             return note;
         }
         throw  new Exception("Not Authorized");
     }

     public void createNote(Note note){
         noteDAO.insert(note);
     }



}
