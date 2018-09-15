package pidscrypt.world.mutual.mutal.api;

import java.util.Date;

public class TextMessage extends ChatMessage {

    public TextMessage() {
        super();
    }

    public TextMessage(String senderId, String receiverId, String message) {
        super(senderId, receiverId, message, MessageType.TEXT);
        setMessageStatus(MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER);
    }
}
