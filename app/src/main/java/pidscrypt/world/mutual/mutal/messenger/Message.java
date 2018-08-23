package pidscrypt.world.mutual.mutal.messenger;

import java.util.Date;

public class Message {
    private int status;
    private String message;
    private Date sent_timestamp;
    private String by_uid, to_uid;

    public Message() {
        //required
    }

    public Message(int status, String message, Date sent_timestamp, String by_uid, String to_uid) {
        this.status = status;
        this.message = message;
        this.sent_timestamp = sent_timestamp;
        this.by_uid = by_uid;
        this.to_uid = to_uid;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Date getSent_timestamp() {
        return sent_timestamp;
    }

    public String getBy_uid() {
        return by_uid;
    }

    public String getTo_uid() {
        return to_uid;
    }
}
