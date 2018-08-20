package pidscrypt.world.mutual.mutal.user;

public class User {
    private String name,
            status_text,
            phone,
            image;

    public User() {

    }

    public User(String name, String status_text, String phone, String image) {
        this.name = name;
        this.status_text = status_text;
        this.phone = phone;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getStatus_text() {
        return status_text;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }
}
