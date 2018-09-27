package pidscrypt.world.mutual.mutal.api;


public class Contact {

        private String name;
        private String number;
        private int img;
        private String tag;
        private String image_uri;
        private int isMutual = 1;

    public Contact(String name, String number, String image_uri) {
        this.name = name;
        this.number = number;
        this.image_uri = image_uri;
    }

    public void setMutual(int mutual) {
        this.isMutual = mutual;
    }

    public int isMutual() {
        return isMutual;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTag() {
        return tag;
    }


    public int getImg() {
        return img;
    }


    public String getImage_uri() {
        return image_uri;
    }
}
