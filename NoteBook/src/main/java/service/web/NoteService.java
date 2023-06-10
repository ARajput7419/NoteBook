package service.web;

import database.entity.Note;
import database.repository.NoteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

     private List<String> extractUrls(String content,HttpServletRequest request){
         List<String> result = new ArrayList<>();
         String regex = "\\W"+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"(.+)"+"\\W";
         Pattern p = Pattern.compile(regex);
         Matcher matcher = p.matcher(content);
         while (matcher.find()){
             String url = matcher.group(1);
             result.add(url);
         }
         return result;
     }


     @Transactional
     public void createNote(Note note, HttpServletRequest request){

         String content  = note.getContent();
         List<String> locations = extractUrls(content,request);
         noteDAO.insert(note);
     }




}
