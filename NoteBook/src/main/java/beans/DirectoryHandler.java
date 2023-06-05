package beans;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class DirectoryHandler {


    private String cwd;


    public void setCwd(String cwd){
        this.cwd = cwd;
    }

    public String getCwd(){
        return cwd;
    }

    public boolean createFile(String filename){

        File file = new File(getCwd()+"/"+filename);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        }

    }

    public boolean write(String filename,String data) throws IOException {


        File file = new File(getCwd()+"/"+filename);
        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.flush();

        return true;

    }

    public boolean write(String filename , MultipartFile multipartFile) throws IOException {

        File file = new File(getCwd()+"/"+filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());
        outputStream.close();
        return true;
    }

    public boolean delete(String filename){
        File file = new File(getCwd()+"/"+filename);
        if (file.isDirectory()) return false;
        return file.delete();
    }

    public boolean createDirectory(String dirName){

        File file = new File(getCwd()+"/"+dirName);

        return file.mkdir();

    }

    public boolean deleteDirectory(String dirName){
        Path path = Paths.get(getCwd()+"/"+ dirName);
        if (Files.exists(path)) {
            try {
                Files.walk(path)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);

                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return true;
        }
    }

}
