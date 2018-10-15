package pidscrypt.world.mutual.mutal.api;

public class DocumentMessage extends ChatMessage {



    public DocumentMessage() {
        super();
    }

    public DocumentMessage(String senderId, String receiverId, String name, String localFile) {
        super(senderId, receiverId, name, MessageType.DOCUMENT);
        setMessageStatus(MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA);
        setLocalFile(localFile);
    }

}