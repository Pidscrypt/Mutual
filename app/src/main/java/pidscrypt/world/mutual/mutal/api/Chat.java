package pidscrypt.world.mutual.mutal.api;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    private String name,
            message,
            unreadMessages;
    private int messageStatus;
    private String imageUri;
    private Date time;

    public Chat(){
        // needed constructor
    }

    public Chat(String name, String message, String unreadMessages) {
        this.name = name;
        this.message = message;
        this.unreadMessages = unreadMessages;
    }

    public Chat(String name, String message, String unreadMessages, int messageStatus, String imageUri, Date time) {
        this.name = name;
        this.message = message;
        this.unreadMessages = unreadMessages;
        this.messageStatus = messageStatus;
        this.imageUri = imageUri;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getUnreadmessages() {
        return unreadMessages;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public String getImageUri() {
        return imageUri;
    }
    @ServerTimestamp
    public Date getTime() {
        return time;
    }
}
