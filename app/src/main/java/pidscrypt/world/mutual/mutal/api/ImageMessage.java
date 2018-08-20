package pidscrypt.world.mutual.mutal.api;

import java.net.URI;

public class ImageMessage extends ChatMessage {

    public ImageMessage() {

    }

    public ImageMessage(String sender, String messageImage, long messageTime, int status) {
        super(sender, messageImage,messageTime,status);
        super.setMessageType(MessageType.IMAGE);
    }

}
