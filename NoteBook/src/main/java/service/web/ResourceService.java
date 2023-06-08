package service.web;

import database.entity.Resource;
import database.repository.ResourceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceDAO resourceDAO;

    public int totalResource(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return resourceDAO.totalResources(username);
    }

    public List<Resource> getAll(int page_size,int offset){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return resourceDAO.getAll(username,page_size,offset);
    }


}
