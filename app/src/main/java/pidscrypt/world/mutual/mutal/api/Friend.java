package pidscrypt.world.mutual.mutal.api;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import pidscrypt.world.mutual.mutal.ContactsFragment;

@IgnoreExtraProperties
public class Friend {

    private int numReadMsg;
    private int lastMsgStatus;
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("profile_pic_url")
    @Expose
    private String profilePicUrl = "";
    @SerializedName("last_seen")
    @Expose
    private String lastSeen = "";
    @SerializedName("is_typing")
    @Expose
    private Boolean isTyping = false;
    @SerializedName("status")
    @Expose
    private String status = "";

    @SerializedName("uid")
    @Expose
    private String uid = "";

    @SerializedName("lastMsg")
    @Expose
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
