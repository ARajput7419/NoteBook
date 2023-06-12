package controller.resources.api;
import beans.DirectoryHandler;
import database.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import responses.resources.DeleteStatus;
import responses.resources.UpdateStatus;
import responses.resources.UploadStatus;
import service.web.ResourceService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RequestMapping("/api/resources")
@RestController
public class RestResourcesController {

    @Autowired
    private DirectoryHandler directoryHandler;

    @Value("${resources}")
    private String resourceDirectory;

    @Autowired
    private ResourceService resourceService;

    @Value("${page_size}")
    private int page_size;

    private String getUrl(String fileName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        return resourceDirectory+"/"+username+"/"+fileName;

    }

    @ExceptionHandler({IOException.class,RuntimeException.class})
    public ResponseEntity<UploadStatus> ioExceptionHandler(){
        UploadStatus status = new UploadStatus();
        status.setMessage("Internal Server Error");
        return new ResponseEntity<>(status,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> genericException(Exception e){

        if (e.getMessage().equals("Bad Request")) return new ResponseEntity<>("Bad Request",HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteStatus> delete(@PathVariable int id){
        DeleteStatus deleteStatus = new DeleteStatus();
        ResourceService.Status status = resourceService.delete(id,directoryHandler);
        if (status == ResourceService.Status.NOT_AUTHORIZED){
            deleteStatus.setMessage("Not Authorized");
            return new ResponseEntity<>(deleteStatus,HttpStatus.UNAUTHORIZED);
        }
        else if(status == ResourceService.Status.RESOURCE_DOES_NOT_EXISTS){
            deleteStatus.setMessage("Resource Does Not Exists");
            return new ResponseEntity<>(deleteStatus,HttpStatus.NOT_FOUND);
        }
        else{
            deleteStatus.setMessage("Resource Deleted Successfully");
            return new ResponseEntity<>(deleteStatus,HttpStatus.OK);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateStatus> updateVisibility(@RequestParam("visibility") String visibility, int id){
        UpdateStatus updateStatus = new UpdateStatus();
        Resource resource = new Resource();
        ResourceService.Status status = resourceService.updateVisibility(resource,id,visibility);
        if (status == ResourceService.Status.RESOURCE_DOES_NOT_EXISTS){
            updateStatus.setMessage("Resource Does Not Exists");
            return new ResponseEntity<>(updateStatus,HttpStatus.NOT_FOUND);
        }
        else if (status == ResourceService.Status.NOT_AUTHORIZED){
            updateStatus.setMessage("Not Authorized");
            return new ResponseEntity<>(updateStatus,HttpStatus.UNAUTHORIZED);
        }
        else if (status == ResourceService.Status.BAD_REQUEST){
            updateStatus.setMessage("Bad Request");
            return new ResponseEntity<>(updateStatus,HttpStatus.BAD_REQUEST);
        }
        else{
            updateStatus.setCurrent_visibility(resource.getVisibility());
            updateStatus.setMessage("Visibility Updated Successfully");
            return new ResponseEntity<>(updateStatus,HttpStatus.OK);
        }
    }

    public String createUserDirectoryIfNotExists(){
        String username = getUsername();
        boolean status = directoryHandler.createDirectory(username);
        if (status){
            return directoryHandler.getCwd()+"/"+username;
        }
        else
            return null;
    }

    public String getUsername(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @PostMapping("/")
    public ResponseEntity<UploadStatus> upload(@RequestParam("file")MultipartFile multipartFile, @RequestParam("visibility") String visibility,HttpServletRequest request) throws IOException {
        UploadStatus uploadStatus = new UploadStatus();
        Resource new_resource = new Resource();
        new_resource.setVisibility(visibility);
        new_resource.setTimestamp(new Timestamp(System.currentTimeMillis()));

        if (multipartFile != null){
            String fileName = multipartFile.getOriginalFilename();
            String url = getUrl(fileName);
            String link = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getServletContext().getContextPath()+"/"+url;
            new_resource.setLocation(url);
            uploadStatus.setUrl(link);
            String directory = createUserDirectoryIfNotExists();
            if (directory != null){
                directoryHandler.setCwd(directory);
                ResourceService.Status status = resourceService.upload(multipartFile,fileName,directoryHandler,new_resource);
                if (status == ResourceService.Status.OK){
                    uploadStatus.setMessage("Uploaded Successfully");
                    return new ResponseEntity<>(uploadStatus,HttpStatus.OK);
                }
                else if (status== ResourceService.Status.ALREADY_EXISTS){
                    uploadStatus.setMessage("Resource Already Exists");
                    return new ResponseEntity<>(uploadStatus,HttpStatus.OK);
                }
                else{
                    uploadStatus.setMessage("Internal Server Error");
                    return new ResponseEntity<>(uploadStatus,HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else{
                uploadStatus.setMessage("Internal Server Error");
                return new ResponseEntity<>(uploadStatus,HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        else {
            uploadStatus.setMessage("Invalid Request");
            return new ResponseEntity<>(uploadStatus,HttpStatus.BAD_REQUEST);
        }

    }

    private class Page{
        private int total_pages;
        private List<Resource> resources;

        public Page(int total_pages, List<Resource> resources) {
            this.total_pages = total_pages;
            this.resources = resources;
        }

        public Page() {
        }

        public int getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(int total_pages) {
            this.total_pages = total_pages;
        }

        public List<Resource> getResources() {
            return resources;
        }

        public void setResources(List<Resource> resources) {
            this.resources = resources;
        }
    }

    @GetMapping("/{page_number}")
    public Page getResourcesPrivate(@PathVariable int page_number,@RequestParam("keyword") String keyword) throws Exception {
        if (page_number <= 0 ) throw new Exception("Page Number Invalid");

        if (keyword == null || keyword.trim().length() == 0) return getResourcesPrivateWithoutKeyword(page_number);

        List<Resource> resources = resourceService.searchByKeywordPrivate(keyword,page_size,(page_number-1)*page_size);
        int total_resources = resourceService.searchByKeywordPrivateCount(keyword);
        int total_pages = total_resources/page_size + (total_resources%page_size==0?0:1);
        Page page = new Page();
        page.setResources(resources);
        page.setTotal_pages(total_pages);
        return page;
    }

    public Page getResourcesPrivateWithoutKeyword(int page_number){
        List<Resource> resources = resourceService.getResourcesPrivate(page_size,(page_number-1)*page_size);
        int total_resources = resourceService.getResourcesPrivateCount();
        int total_pages = total_resources/page_size + (total_resources%page_size==0?0:1);
        Page page = new Page();
        page.setResources(resources);
        page.setTotal_pages(total_pages);
        return page;
    }

}
