package pidscrypt.world.mutual.mutal.api;

import java.util.Date;
import java.util.List;

import pidscrypt.world.mutual.mutal.user.MutualUser;

public class Feed {

    private int like_count;
    private List<String> images;
    private long timestamp;
    private List<Comment> comments;
    private List<Like> likes;
    private List<Tag> tags;
    private MutualUser feed_owner;

    public Feed() {
    }

    public Feed(List<String> images, List<Comment> comments, List<Like> likes, List<Tag> tags, MutualUser feed_owner) {
        this.images = images;
        this.comments = comments;
        this.likes = likes;
        this.tags = tags;
        this.feed_owner = feed_owner;
        this.like_count = 0;
        this.timestamp = new Date().getTime();
    }

    public int getLike_count() {
        return like_count;
    }

    public List<String> getImages() {
        return images;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public MutualUser getFeed_owner() {
        return feed_owner;
    }
}
