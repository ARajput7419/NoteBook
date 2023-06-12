package controller.notes.api;
import database.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import responses.resources.DeleteStatus;
import service.web.NoteService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class RestNoteController {


    @Autowired
    private NoteService noteService;

    @Value("${page_size}")
    private int page_size;


    private class Page{

        List<Note> notes;
        private int total_pages;

        public List<Note> getNotes() {
            return notes;
        }

        public void setNotes(List<Note> notes) {
            this.notes = notes;
        }

        public int getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(int total_pages) {
            this.total_pages = total_pages;
        }
    }

    @ExceptionHandler
    public ResponseEntity<String> exceptionHandler(Exception e ){
        if (e.equals("Bad Request")){
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/private/{page_number}")
    public Page getNotesPrivate(@PathVariable int page_number , @RequestParam("keyword") String keyword) throws Exception {

        if (page_number <= 0 ) {
            throw  new Exception("Bad Request");
        }

        if (keyword == null || keyword.trim().length() == 0) return getNotesPrivateWithoutKeyword(page_number);

        int total_notes = noteService.searchByKeywordPrivateCount(keyword);
        int total_pages = total_notes/page_size + (total_notes%page_size==0?0:1);
        List<Note> noteList = noteService.searchByKeywordPrivate(keyword,page_size,(page_number-1)*page_size);
        Page page = new Page();
        page.setTotal_pages(total_pages);
        page.setNotes(noteList);
        return page;
    }

    public Page getNotesPrivateWithoutKeyword(int page_number){

        int total_notes = noteService.getNotesPrivateCount();
        int total_pages = total_notes/page_size + (total_notes%page_size==0?0:1);
        List<Note> noteList = noteService.getNotesPrivate(page_size,(page_number-1)*page_size);
        Page page = new Page();
        page.setTotal_pages(total_pages);
        page.setNotes(noteList);
        return page;
    }

    public Page getNotesPublicWithoutKeyword(int page_number){
        int total_notes = noteService.getNotesPublicCount();
        int total_pages = total_notes/page_size + (total_notes%page_size==0?0:1);
        List<Note> noteList = noteService.getNotesPublic(page_size,(page_number-1)*page_size);
        Page page = new Page();
        page.setTotal_pages(total_pages);
        page.setNotes(noteList);
        return page;
    }

    @GetMapping("/public/{page_number}")
    public Page getNotesPublic(@PathVariable int page_number , @RequestParam("keyword") String keyword) throws Exception {

        if (page_number <= 0 ){
            throw  new Exception("Bad Request");
        }

        if (keyword== null || keyword.trim().length() == 0) return getNotesPublicWithoutKeyword(page_number);

        int total_notes = noteService.searchByKeywordPublicCount(keyword);
        int total_pages = total_notes/page_size + (total_notes%page_size==0?0:1);
        List<Note> noteList = noteService.searchByKeywordPublic(keyword,page_size,(page_number-1)*page_size);
        Page page = new Page();
        page.setTotal_pages(total_pages);
        page.setNotes(noteList);
        return page;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteStatus> deleteNote(@PathVariable int id, HttpServletRequest request){
        DeleteStatus deleteStatus = new DeleteStatus();
        NoteService.Status status = noteService.delete(id,request);
        if (status == NoteService.Status.NOT_AUTHORIZED){
            deleteStatus.setMessage("Not Authorized");
            return new ResponseEntity<>(deleteStatus,HttpStatus.UNAUTHORIZED);
        }
        else if (status == NoteService.Status.RESOURCE_DOES_NOT_EXISTS){
            deleteStatus.setMessage("Resource Does Not Exists");
            return new ResponseEntity<>(deleteStatus,HttpStatus.NOT_FOUND);
        }
        else{
            deleteStatus.setMessage("Resource is  Deleted Successfully");
            return new ResponseEntity<>(deleteStatus,HttpStatus.OK);
        }

    }
}
