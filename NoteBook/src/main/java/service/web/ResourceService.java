package service.web;

import beans.DirectoryHandler;
import database.entity.Resource;
import database.entity.User;
import database.repository.ResourceDAO;
import database.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import responses.resources.UploadStatus;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceDAO resourceDAO;
    @Autowired
    private UserDAO userDAO;

    public int getResourcesPrivateCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return resourceDAO.getResourcesPrivateCount(username);
    }

    public List<Resource> getResourcesPrivate(int page_size,int offset){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return resourceDAO.getResourcesPrivate(username,page_size,offset);
    }

    public List<Resource> searchByKeywordPrivate(String keyword,int page_size , int offset){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return resourceDAO.searchByKeywordPrivate(username,keyword,page_size,offset);
    }
    public int searchByKeywordPrivateCount(String keyword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return resourceDAO.searchByKeywordPrivateCount(username,keyword);
    }

    public enum Status{
        ALREADY_EXISTS,OK,FAILED,NOT_AUTHORIZED,BAD_REQUEST,RESOURCE_DOES_NOT_EXISTS
    }

    @Transactional(rollbackOn = {IOException.class})
    public Status upload(MultipartFile multipartFile, String filename, DirectoryHandler directoryHandler,Resource resource) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        User user = userDAO.get(username);
        resource.setUser(user);
        resource.setName(filename);
        boolean status = resourceDAO.checkIfAlreadyExists(resource.getLocation());
        if (status){
            return Status.ALREADY_EXISTS;
        }
        else{
            resourceDAO.insert(resource);
            directoryHandler.write(filename,multipartFile);
            return Status.OK;
        }
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public Status delete(int id,DirectoryHandler directoryHandler){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        Resource resource = resourceDAO.get(id);
        if (resource == null) return Status.RESOURCE_DOES_NOT_EXISTS;
        if (!resource.getUser().getEmail().equals(username)){
            return Status.NOT_AUTHORIZED;
        }
        else{
            resourceDAO.delete(id);
            String filename = "../"+resource.getLocation();
            boolean status = directoryHandler.delete(filename);
            if (!status){
                throw new RuntimeException("Not able to delete");
            }
            else{
                return Status.OK;
            }
        }
    }

    @Transactional
    public Status updateVisibility(Resource resource,int id,String visibility){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        if (visibility == null || (!visibility.equals("Public") && !visibility.equals("Private"))) return Status.BAD_REQUEST;
        Resource actual_resource = resourceDAO.get(id);
        if (actual_resource == null){
            return Status.RESOURCE_DOES_NOT_EXISTS;
        }
        else{
            if (actual_resource.getUser().getEmail().equals(username)){
                actual_resource.setVisibility(visibility);
                resourceDAO.update(actual_resource);
                resource.setVisibility(actual_resource.getVisibility());
                return Status.OK;
            }
            else{
                return Status.NOT_AUTHORIZED;
            }
        }
    }


}
