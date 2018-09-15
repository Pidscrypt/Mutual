package pidscrypt.world.mutual.mutal.api;

import pidscrypt.world.mutual.mutal.user.MutualUser;

public class Comment {
    private String comment;
    private MutualUser comment_by;
    private long timestamp;

    public Comment() {
    }

    public Comment(String comment, MutualUser comment_by, long timestamp) {
        this.comment = comment;
        this.comment_by = comment_by;
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public MutualUser getComment_by() {
        return comment_by;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
