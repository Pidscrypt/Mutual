package pidscrypt.world.mutual.mutal.user;

import android.net.Uri;

import java.util.Date;
import java.util.List;

import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.UserStatus;

public class MutualUser {
    private String name,
            phone,
            image_uri = "",
            status = "",
            uid;
    private long last_seen;
    private boolean online;

    public MutualUser() {
    }

    public MutualUser(String name, String phone, String image_uri, String status, String uid, long last_seen, boolean online) {
        this.name = name;
        this.phone = phone;
        this.image_uri = image_uri;
        this.status = status;
        this.uid = uid;
        this.last_seen = last_seen;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public String getStatus() {
        return status;
    }

    public String getUId() {
        return uid;
    }

    public long getLast_seen() {
        return last_seen;
    }

    public boolean isOnline() {
        return online;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
