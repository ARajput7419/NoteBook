package controller.web;


import database.entity.Note;
import database.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.web.NoteService;
import service.web.ResourceService;
import service.web.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;


@RequestMapping("/notes")
@Controller
public class NoteController {

    @Autowired
    private NoteService service;

    @Autowired
    private ResourceService resourceService;

    @Value("${page_size}")
    private int page_size;

    @Autowired
    private UserService userService;

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("note",new Note());
        return "creates";
    }



    @GetMapping("/edit")
    public String edit(@RequestParam("id") int id,Model model) {

        return service.requestForEdit(id, model);

    }

    @PutMapping("/edit")
    public String edit(@ModelAttribute("note") Note note,Model model,HttpServletRequest request){
        note.setUser(userService.getByUsername(getUserName()));
        service.update(note,request);
        model.addAttribute("message","Note is Updated Successfully");
        return "edit";
    }


    private String getUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute("note") Note note, Model model, HttpServletRequest request){
        note.setTimestamp(new Timestamp(System.currentTimeMillis()));
        note.setUser(userService.getByUsername(getUserName()));
        try{
            service.createNote(note,request);
            model.addAttribute("message","Created Successfully");
        }
        catch (Exception e){
           model.addAttribute("message","Failed");
        }
        return "creates";
    }

    private String getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass() == AnonymousAuthenticationToken.class) return null;
        else return (String) authentication.getPrincipal();
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id , HttpServletResponse response,Model model) throws IOException {
        try {
            Note note = service.getNote(id);
            if (note == null){
                model.addAttribute("message","Not Available");
                return "error";
            }
            String user = getUser();
            if (user != null && note.getUser().getEmail().equals(user)) model.addAttribute("edit",true);
            if (user != null) model.addAttribute("login",true);
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
    @GetMapping("/private")
    public String myNotes(Model model,HttpServletRequest request){
        int total_notes = service.getNotesPrivateCount();
        List<Note> notes = service.getNotesPrivate(page_size,0);
        int total_pages_notes = total_notes/page_size + (total_notes%page_size!=0?1:0);
        int total_resources = resourceService.getResourcesPrivateCount();
        List<Resource> resources = resourceService.getResourcesPrivate(page_size,0);
        for (Resource resource : resources){
            String link = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getServletContext().getContextPath()+"/"+resource.getLocation();
            resource.setLocation(link);
        }
        int total_pages_resources = total_resources/page_size + (total_resources%page_size!=0?1:0);
        model.addAttribute("total_pages_notes",total_pages_notes);
        model.addAttribute("total_pages_resources",total_pages_resources);
        model.addAttribute("notes",notes);
        model.addAttribute("resources",resources);
        return "my_notes";
    }

    @GetMapping("/public")
    public String publicNotes(Model model,HttpServletRequest request){
        String keyword = request.getParameter("keyword");
        if (keyword.trim().length()!=0) {
            publicNotesByKeyword(model,keyword);
            return "public_notes";
        }
        int total_notes = service.getNotesPublicCount();
        List<Note> notes = service.getNotesPublic(page_size,0);
        int total_pages_notes = total_notes/page_size + (total_notes%page_size!=0?1:0);
        model.addAttribute("total_pages_notes",total_pages_notes);
        model.addAttribute("notes",notes);
        return "public_notes";
    }

    private void publicNotesByKeyword(Model model,String keyword){
       int total_notes = service.searchByKeywordPublicCount(keyword);
        List<Note> notes = service.searchByKeywordPublic(keyword,page_size,0);
        int total_pages_notes = total_notes/page_size + (total_notes%page_size!=0?1:0);
        model.addAttribute("total_pages_notes",total_pages_notes);
        model.addAttribute("notes",notes);
        model.addAttribute("keyword",keyword);
    }




}
