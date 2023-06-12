package service.web;

import database.entity.Note;
import database.repository.NoteDAO;
import database.repository.ResourceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

     @Value("${resources}")
     private String resourceDirectory;

     @Autowired
     private ResourceDAO resourceDAO;

     @Transactional
     public List<Note> searchByKeywordPublic(String keyword,int count,int offset){
         return noteDAO.searchByKeywordPublic(keyword,count,offset);
     }

     @Transactional
     public List<Note> searchByKeywordPrivate(String keyword,int count,int offset){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = (String) authentication.getPrincipal();
         return noteDAO.searchByKeywordPrivate(keyword,username,count,offset);
     }

     @Transactional
    public int searchByKeywordPublicCount(String keyword){
        return noteDAO.searchByKeywordPublicCount(keyword);
    }

    @Transactional
    public int searchByKeywordPrivateCount(String keyword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return noteDAO.searchByKeywordPrivateCount(keyword,username);
    }

    @Transactional
    public List<Note> getNotesPrivate(int count,int offset){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return noteDAO.getNotesPrivate(username,count,offset);
    }

    @Transactional
    public int getNotesPrivateCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return noteDAO.getNotesPrivateCount(username);
    }


    @Transactional
    public List<Note> getNotesPublic(int count,int offset){
        return noteDAO.getNotesPublic(count,offset);
    }

    @Transactional
    public int getNotesPublicCount(){
        return noteDAO.getNotesPublicCount();
    }

    @Transactional
     public Note getNote(int id) throws Exception {
         Note note = noteDAO.get(id);
         if (note == null) return null;
         if (note.getVisibility().equals("Public")) return note;
         Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null) throw new Exception("Not Authenticated");
         String username = (String) authentication.getPrincipal();
         if (note.getUser().getEmail().equals(username)){
             return note;
         }
         throw  new Exception("Not Authorized");
     }

    @Transactional
    public Note getNoteWithoutAuthentication(int id) {
        Note note = noteDAO.get(id);
        return note;
    }


     private List<String> extractUrls(String content,HttpServletRequest request){
         String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         username = username.replace(".","\\.");
         List<String> result = new ArrayList<>();
         String regex = "\\W"+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/("+resourceDirectory+"/"+username+"/\\w+"+"\\.?\\w+)"+"\\W";
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
         resourceDAO.increment(locations);
         noteDAO.insert(note);

     }

    public enum Status{
        ALREADY_EXISTS,OK,NOT_AUTHORIZED,BAD_REQUEST,RESOURCE_DOES_NOT_EXISTS
    }

     @Transactional
     public Status delete(int id,HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        Note note = noteDAO.get(id);
        if (note == null){
            return Status.RESOURCE_DOES_NOT_EXISTS;
        }
        else {
            if(note.getUser().getEmail().equals(username)){
                String content = note.getContent();
                List<String> locations = extractUrls(content,request);
                resourceDAO.decrement(locations);
                noteDAO.delete(id);
                return Status.OK;
            }
            else{
                return Status.NOT_AUTHORIZED;
            }

        }

     }

     @Transactional
     public String requestForEdit(int id, Model model){
         String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Note note = noteDAO.get(id);
         if (note == null){
             model.addAttribute("message","Resource Does Not Exists");
             return "error";
         }
         else{
             if (note.getUser().getEmail().equals(username)){
                 model.addAttribute("note",note);
                 return "edit";
             }
             else{
                 model.addAttribute("message","Not Authorized");
                 return "error";
             }
         }
     }

     @Transactional
    public void update(Note note , HttpServletRequest request) {

         delete(note.getId(),request);
         createNote(note,request);

     }






}
