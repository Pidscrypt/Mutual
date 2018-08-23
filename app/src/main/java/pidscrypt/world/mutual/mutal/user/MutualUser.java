package pidscrypt.world.mutual.mutal.user;

import android.net.Uri;

public class MutualUser {
    private String name;
    private String phone;
    private String image_uri;
    private String tag;

    public MutualUser() {
    }

    public MutualUser(String name, String phone, String image_uri, String tag) {
        this.name = name;
        this.phone = phone;
        this.image_uri = image_uri;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }
}
