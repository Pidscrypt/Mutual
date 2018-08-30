package pidscrypt.world.mutual.mutal.api;

import java.net.URI;
import java.util.Date;

public class ImageMessage extends ChatMessage {

    public ImageMessage() {
        super();
    }

    public ImageMessage(String senderId, String receiverId, String imageUri) {
        super(senderId, receiverId, imageUri, MessageType.IMAGE);
    }
}
