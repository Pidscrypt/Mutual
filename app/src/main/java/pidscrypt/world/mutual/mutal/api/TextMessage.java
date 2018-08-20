package pidscrypt.world.mutual.mutal.api;

import java.util.Date;

public class TextMessage extends ChatMessage {

    public TextMessage() {

    }

    public TextMessage(String sender, String messageText, long messageTime, int status) {
        super(sender, messageText,messageTime,status);
        super.setMessageType(MessageType.TEXT);
    }

}
