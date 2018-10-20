package pidscrypt.world.mutual.mutal.api;

import java.net.URI;
import java.util.Date;

public class ImageMessage extends ChatMessage {

    public ImageMessage() {
        super();
    }

    public ImageMessage(String senderId, String receiverId, String imageUri, String localFile) {
        super(senderId, receiverId, imageUri, MessageType.IMAGE);
        setMessageStatus(MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA);
        setLocalFile(localFile);
    }

    public void  testMethod(){

    }

}
