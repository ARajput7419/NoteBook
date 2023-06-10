package responses.resources;

public class UpdateStatus {

    private String current_visibility;
    private String message;

    public String getCurrent_visibility() {
        return current_visibility;
    }

    public void setCurrent_visibility(String current_visibility) {
        this.current_visibility = current_visibility;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
