package pidscrypt.world.mutual.mutal.user;

import android.net.Uri;

import java.util.Date;
import java.util.List;

import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.UserStatus;

public class MutualUser {
    private String name,
            phone,
            image_uri,
            status,
            uid;
    private long last_seen;
    private int online_status;

    public MutualUser() {
    }

    public MutualUser(String name, String phone, String user_id) {
        this.name = name;
        this.phone = phone;
        this.uid = user_id;
        this.last_seen = new Date().getTime();
        this.online_status = UserStatus.ONLINE;
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

    public int getOnline_status() {
        return online_status;
    }


}
