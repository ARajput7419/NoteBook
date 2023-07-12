package controller.execution.api;

import beans.DirectoryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import responses.execution.CodeInput;
import responses.execution.CodeOutput;
import responses.execution.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Timestamp;
import java.util.HashMap;

@RequestMapping("/api/execution")
@RestController
public class API {

    @Autowired
    private DirectoryHandler directoryHandler;

    @Value("${destination:/}")
    private String destination_folder;

    @Qualifier("language")
    @Autowired
    HashMap<String,String[]> properties;


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handler(Exception exception){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setException(exception.getMessage());
        errorResponse.setTimestamp((new Timestamp(System.currentTimeMillis()).toString()));

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getStringRepresentation(BufferedReader reader) throws IOException {

        StringBuilder output = new StringBuilder();
        int ch = 0;
        while((ch=reader.read())!=-1){
            output.append((char) ch);
        }
        return output.toString();

    }

    private void validate(CodeInput codeInput) throws Exception {

        if (!properties.containsKey(codeInput.getLang())) throw new Exception("Invalid language");


    }


    @PostMapping("/exec")
    public CodeOutput response(@RequestBody CodeInput codeInput ,  HttpServletRequest request) throws Exception {


        validate(codeInput);

        String lang = codeInput.getLang();

        String ip = request.getRemoteAddr().replace(":","_").replace(".","_");

        boolean status = directoryHandler.createDirectory(ip);

        if (!status) throw new Exception("Failed ...");

        String filename = "code."+ properties.get(lang)[0];

        status = directoryHandler.createFile(ip+"/"+filename);

        if (!status) throw new Exception("Failed ...");

        status = directoryHandler.write(ip+"/"+filename,codeInput.getCode());

        if (!status)  throw  new Exception("Failed ...");

        String command = properties.get(lang+"_command")[0];

        if (lang.equals("cpp")) {


            ProcessBuilder builder = new ProcessBuilder(new String[]{command, filename});

            builder.redirectErrorStream(true);

            builder.directory(new File(directoryHandler.getCwd()+"/"+ip));

            Process compiler = builder.start();

            int exit_code = compiler.waitFor();

            if (exit_code != 0){

                CodeOutput output = new CodeOutput();

                output.setOutput(getStringRepresentation(compiler.inputReader()));

                return output;

            }

            command = "./a.out";

            filename="";


        }

        ProcessBuilder builder = new ProcessBuilder(new String[]{command, filename});

        builder.redirectErrorStream(true);

        builder.directory(new File(directoryHandler.getCwd()+"/"+ip));

        Process process = builder.start();

        BufferedWriter writer = process.outputWriter();

        writer.write(codeInput.getInput());

        writer.close();


        BufferedReader output_reader = process.inputReader();


        CodeOutput codeOutput = new CodeOutput();

        codeOutput.setOutput(getStringRepresentation(output_reader));


        return codeOutput;


    }

}
