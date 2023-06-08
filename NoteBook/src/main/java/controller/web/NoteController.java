package controller.web;


import database.entity.Note;
import database.entity.Resource;
import org.apache.catalina.authenticator.SpnegoAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.web.NoteService;
import service.web.ResourceService;

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


    @RequestMapping(value = "/public/search",method = RequestMethod.GET)
    public String noteSearch(@RequestParam("keyword") String keyword, Model model){
        if (keyword == null){
            model.addAttribute("message","Bad Request");
            return "error";
        }
        else {
            List<Note> noteList = service.searchNotes(keyword);
            model.addAttribute("note_list",noteList);
            return "notes";
        }
    }

    @GetMapping("/private/search")
    public String privateSearch(@RequestParam("Keyword") String keyword,Model model){
        if (keyword == null){
            model.addAttribute("message","Bad Request");
            return "error";
        }
        else{
            List<Note> noteList = service.privateSearch(keyword);
            model.addAttribute("note_list",noteList);
            return "notes";
        }
    }

    @GetMapping("/create")
    public String create(){
        return "creates";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();

        return "view";
    }


    @GetMapping("/")
    public String myNotes(Model model){
        int total_notes = service.totalNotes();
        List<Note> notes = service.getAll(page_size,0);
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
