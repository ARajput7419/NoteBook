package service.web;

import database.entity.Resource;
import database.repository.ResourceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginationService {

    @Autowired
    private ResourceDAO resourceDAO;

    public List<Resource> getResources(int page_size , int  page_number){

        //resourceDAO.get();
        return null;
    }

}
