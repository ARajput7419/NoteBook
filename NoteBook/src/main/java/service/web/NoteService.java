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
     public Note getNote(int id){
         Note note = noteDAO.get(id);
        // if (note.getUser().getEmail().equals())
         return note;
     }


}
