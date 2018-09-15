package pidscrypt.world.mutual.mutal.api;

import pidscrypt.world.mutual.mutal.user.MutualUser;

public class Conversation {
    private boolean seen;
    private long timestamp;
    private long start_date;
    private String lastMsg;
    private String img_uri;
    private int last_msg_status;
    private int count = 0;
    private String with;
    private MutualUser user;

    public Conversation() {
    }

    public Conversation(boolean seen, long start_date, String img_uri, MutualUser user) {
        this.seen = seen;
        this.start_date = start_date;
        this.img_uri = img_uri;
        this.last_msg_status = MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER;
        this.user = user;
    }

    public MutualUser getUser() {
        return user;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public boolean isSeen() {
        return seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getStart_date() {
        return start_date;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public int getLast_msg_status() {
        return last_msg_status;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getCount() {
        return count;
    }
}
