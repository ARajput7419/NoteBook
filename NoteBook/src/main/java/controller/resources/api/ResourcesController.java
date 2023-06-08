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
import responses.resources.UploadStatus;
import service.web.ResourceService;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/resources")
@RestController
public class ResourcesController {

    @Autowired
    private DirectoryHandler directoryHandler;

    @Value("${resources}")
    private String resourceDirectory;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private int page_size;

    private String getUrl(String fileName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String)authentication.getPrincipal();
        return resourceDirectory+"/"+username+"/"+fileName;

    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<UploadStatus> ioExceptionHandler(){
        UploadStatus status = new UploadStatus();
        status.setMessage("Internal Server Error");
        return new ResponseEntity<>(status,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> genericException(Exception e){
        return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/")
    public ResponseEntity<DeleteStatus> delete(@RequestParam("filename") String filename){
        DeleteStatus deleteStatus = new DeleteStatus();
        if (filename == null){
            deleteStatus.setMessage("Invalid Request");
            return new ResponseEntity<>(deleteStatus,HttpStatus.BAD_REQUEST);
        }
        boolean status = directoryHandler.delete(filename);
        if (status){
            deleteStatus.setMessage("Deleted Successfully");
            return new ResponseEntity<>(deleteStatus,HttpStatus.OK);
        }
        else{
            deleteStatus.setMessage("Internal Server Error");
            return new ResponseEntity<>(deleteStatus,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //return null;
    }


    @PostMapping("/")
    public ResponseEntity<UploadStatus> upload(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        UploadStatus uploadStatus = new UploadStatus();
        if (multipartFile != null){
            String fileName = multipartFile.getOriginalFilename();
            boolean status = directoryHandler.write(fileName,multipartFile);
            if (!status) {
                uploadStatus.setMessage("Unable to  upload the resource");
                return new ResponseEntity<>(uploadStatus,HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else{
                uploadStatus.setMessage("Uploaded Successfully");
                uploadStatus.setUrl(getUrl(fileName));
                return new ResponseEntity<>(uploadStatus,HttpStatus.OK);
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
    public Page get(@PathVariable int page_number) throws Exception {
        if (page_number <= 0 ) throw new Exception("Page Number Invalid");
        List<Resource> resources = resourceService.getAll(page_size,(page_number-1)*page_size);
        int total_resources = resourceService.totalResource();
        int total_pages = total_resources/page_size + (total_resources%page_size==0?0:1);
        Page page = new Page();
        page.setResources(resources);
        page.setTotal_pages(total_pages);
        return page;
    }

}
