package controller.notes.api;

import database.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.web.NoteService;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {


    @Autowired
    private NoteService noteService;

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

    @GetMapping("/{page_number}")
    public Page getNotes(@PathVariable int page_number , @RequestParam("keyword") String keyword,@RequestParam("search")String searchType) throws Exception {
        if (searchType == null || (!searchType.equals("Public") &&  !searchType.equals("Private"))){
            throw new Exception("Bad Request");
        }
        if (searchType.equals("Public")){
            //noteService.searchNotes(keyword)
        }
        return null;
    }
}
