package pidscrypt.world.mutual.mutal.api;

import android.view.Gravity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    private String senderId;

    @SerializedName("message")
    @Expose
    private Object message;
    private long message_time;
    private int messageStatus;

    @SerializedName("type")
    @Expose
    private int messageType;
    private int messageGravity;

    public ChatMessage(){}

    public ChatMessage(String senderId, Object message, long message_time, int messageStatus) {
        this.senderId = senderId;
        this.message = message;
        this.message_time = message_time;
        this.messageStatus = messageStatus;
        if(senderId.equals(FirebaseAuth.getInstance().getUid())){
            messageGravity = Gravity.END;
        }else{
            messageGravity = Gravity.START;
        }
    }

    public int getMessageGravity() {
        return messageGravity;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public long getMessage_time() {
        return message_time;
    }

    public Object getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }
}
