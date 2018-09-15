package pidscrypt.world.mutual.mutal.api;

import pidscrypt.world.mutual.mutal.user.MutualUser;

public class Tag {
    private MutualUser tagged_user, tagged_by;

    public Tag() {
    }

    public Tag(MutualUser tagged_user, MutualUser tagged_by) {
        this.tagged_user = tagged_user;
        this.tagged_by = tagged_by;
    }

    public MutualUser getTagged_user() {
        return tagged_user;
    }

    public MutualUser getTagged_by() {
        return tagged_by;
    }
}
