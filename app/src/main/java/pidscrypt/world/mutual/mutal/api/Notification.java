package pidscrypt.world.mutual.mutal.api;

public class Notification {
    private String from;
    private String message;

    public Notification(){}

    public Notification(String from, String message){
        this.from = from;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }
}
