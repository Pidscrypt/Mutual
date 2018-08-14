package pidscrypt.world.mutual.mutal.api;


import android.widget.ImageView;

public class Friend {

        private String name;
        private String lastMsg;
        private int photo;

    public Friend(String name, String lastMsg, int photo) {
        this.name = name;
        this.lastMsg = lastMsg;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public int getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
