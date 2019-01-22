package pidscrypt.world.mutual.mutal.api;


public class Contact {

        private String name, number, userId, image;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }


    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String uri){
        this.image = uri;
    }
}
