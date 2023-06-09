package controller.web;


import database.entity.Note;
import database.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.web.NoteService;
import service.web.ResourceService;
import service.web.UserService;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService service;

    @Autowired
    private ResourceService resourceService;

    @Value("${page_size}")
    private int page_size;

    @Autowired
    private UserService userService;


//    @RequestMapping(value = "/public/search",method = RequestMethod.GET)
//    public String noteSearch(@RequestParam("keyword") String keyword, Model model){
//        if (keyword == null){
//            model.addAttribute("message","Bad Request");
//            return "error";
//        }
//        else {
//            List<Note> noteList = service.searchNotes(keyword);
//            model.addAttribute("note_list",noteList);
//            return "notes";
//        }
//    }

//    @GetMapping("/private/search")
//    public String privateSearch(@RequestParam("Keyword") String keyword,Model model){
//        if (keyword == null){
//            model.addAttribute("message","Bad Request");
//            return "error";
//        }
//        else{
//            List<Note> noteList = service.privateSearch(keyword);
//            model.addAttribute("note_list",noteList);
//            return "notes";
//        }
//    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("note",new Note());
        return "creates";
    }

    private String getUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

    @GetMapping("/submit")
    public String submit(@ModelAttribute("note") Note note,Model model){
        note.setTimestamp(new Timestamp(System.currentTimeMillis()));
        note.setUser(userService.getByUsername(getUserName()));
        try{
            service.createNote(note);
            model.addAttribute("message","Created Successfully");
        }
        catch (Exception e){
           model.addAttribute("message","Failed");
        }
        return "creates";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id , HttpServletResponse response,Model model) throws IOException {
        try {
            Note note = service.getNote(id);
            if (note == null){
                model.addAttribute("message","Not Available");
                return "error";
            }
            model.addAttribute("note",note);
            return "view";
        }
        catch (Exception e){

            if (e.getMessage().equals("Not Authenticated")){
                response.sendRedirect("/user/login");
                return null;
            }
            else{
                model.addAttribute("message","Not Authorized ");
                return "error";
            }
        }
    }
    @GetMapping("/")
    public String myNotes(Model model){
        int total_notes = service.getNotesPrivateCount();
        List<Note> notes = service.getNotesPrivate(page_size,0);
        int total_pages_notes = total_notes/page_size + (total_notes%page_size!=0?1:0);
        int total_resources = resourceService.totalResource();
        List<Resource> resources = resourceService.getAll(page_size,0);
        int total_pages_resources = total_resources/page_size + (total_resources%page_size!=0?1:0);
        model.addAttribute("total_pages_notes",total_pages_notes);
        model.addAttribute("total_pages_resources",total_pages_resources);
        model.addAttribute("notes",notes);
        model.addAttribute("resources",resources);
        return "my_notes";
    }




}
