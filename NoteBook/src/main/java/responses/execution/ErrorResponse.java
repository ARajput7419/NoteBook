package responses.execution;

import java.sql.Timestamp;

public class ErrorResponse {

    private String exception;

    private String timestamp;


    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
