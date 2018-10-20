package pidscrypt.world.mutual.mutal.api;

import pidscrypt.world.mutual.mutal.user.MutualUser;

public class Conversation {
    private boolean seen;
    private long timestamp;
    private long start_date;
    private String lastMsg;
    private int lastMsgType;
    private String img_uri;
    private int lastMsgStatus;
    private int count = 0;
    private String with, withPhone;
    private String uid;

    public Conversation() {
    }

    public Conversation(boolean seen, long start_date, String img_uri) {
        this.seen = seen;
        this.start_date = start_date;
        this.img_uri = img_uri;
        this.lastMsgStatus = MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER;
    }

    public String getWithPhone() {
        return withPhone;
    }

    public void setWithPhone(String withPhone) {
        this.withPhone = withPhone;
    }

    public int getLastMsgType() {
        return lastMsgType;
    }

    public void setLastMsgType(int lastMsgType) {
        this.lastMsgType = lastMsgType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getLastMsgStatus() {
        return lastMsgStatus;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getCount() {
        return count;
    }
}
