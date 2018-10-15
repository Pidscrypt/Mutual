package pidscrypt.world.mutual.mutal.api;

import android.view.Gravity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String message;
    private long time_sent;
    private long time_recieved;
    private int messageStatus;
    private String localFile, fileName;
    private String firebaseStorageLocation;

    private int messageType;

    ChatMessage(){}

    ChatMessage(String senderId, String receiverId, String message, int messageType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.time_sent = new Date().getTime();
        this.messageType = messageType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFirebaseStorageLocation() {
        return firebaseStorageLocation;
    }

    public void setFirebaseStorageLocation(String firebaseStorageLocation) {
        this.firebaseStorageLocation = firebaseStorageLocation;
    }

    public String getReceiverId() {
        return receiverId;
    }


    public int getMessageType() {
        return messageType;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public long getTime_sent() {
        return time_sent;
    }

    public long getTime_recieved() {
        return time_recieved;
    }

    void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    public String getLocalFile() {
        return localFile;
    }
}
