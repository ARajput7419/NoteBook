package controller.resources.api;
import beans.DirectoryHandler;
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
import java.io.IOException;

@RequestMapping("/resources/api")
@RestController
public class ResourcesController {

    @Autowired
    DirectoryHandler directoryHandler;

    @Value("${resources}")
    String resourceDirectory;

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

}
