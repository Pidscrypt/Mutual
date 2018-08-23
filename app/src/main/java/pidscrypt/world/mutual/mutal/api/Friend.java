package pidscrypt.world.mutual.mutal.api;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.google.firebase.firestore.IgnoreExtraProperties;

import pidscrypt.world.mutual.mutal.ContactsFragment;

@IgnoreExtraProperties
public class Friend {

    private int numReadMsg;
    private int lastMsgStatus;

    private String name = "";

    private String profilePicUrl = "";

    private String lastSeen = "";

    private Boolean isTyping = false;

    private String status = "";

    private String uid = "";

    private String lastMsg = "";

    public int getNumUnReadMsg() {
        return numReadMsg;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getLastMsgStatus() {
        return lastMsgStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Boolean getIsTyping() {
        return isTyping;
    }

    public void setIsTyping(Boolean isTyping) {
        this.isTyping = isTyping;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Friend() {
    }

    public Friend(String uid,String msg,String img, String number, int lastMsgStatus) {
        this.name = number;
        this.profilePicUrl = img;
        this.lastSeen = "10:23";
        this.isTyping = false;
        this.status = "online";
        this.uid= uid;
        this.lastMsg = msg;
        this.lastMsgStatus = lastMsgStatus;
        this.numReadMsg = 3;
    }

}
