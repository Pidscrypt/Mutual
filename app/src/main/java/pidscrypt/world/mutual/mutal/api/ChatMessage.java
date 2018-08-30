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

    private int messageType;

    public ChatMessage(){}

    public ChatMessage(String senderId, String receiverId, String message, int messageType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.time_sent = new Date().getTime();
        this.messageStatus = MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER;
        this.messageType = messageType;
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
}
