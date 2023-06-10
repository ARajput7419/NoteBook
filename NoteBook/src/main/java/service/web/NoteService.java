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

     public List<Note> searchByKeywordPublic(String keyword,int count,int offset){
         return noteDAO.searchByKeywordPublic(keyword,count,offset);
     }

     public List<Note> searchByKeywordPrivate(String keyword,int count,int offset){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = (String) authentication.getPrincipal();
         return noteDAO.searchByKeywordPrivate(keyword,username,count,offset);
     }

    public int searchByKeywordPublicCount(String keyword){
        return noteDAO.searchByKeywordPublicCount(keyword);
    }

    public int searchByKeywordPrivateCount(String keyword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return noteDAO.searchByKeywordPrivateCount(keyword,username);
    }

    public List<Note> getNotesPrivate(int count,int offset){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return noteDAO.getNotesPrivate(username,count,offset);
    }

    public int getNotesPrivateCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return noteDAO.getNotesPrivateCount(username);
    }


    public List<Note> getNotesPublic(int count,int offset){
        return noteDAO.getNotesPublic(count,offset);
    }

    public int getNotesPublicCount(){
        return noteDAO.getNotesPublicCount();
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
