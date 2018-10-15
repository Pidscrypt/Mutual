package pidscrypt.world.mutual.mutal.api;

public class ContactMessage extends ChatMessage {



    public ContactMessage() {
        super();
    }

    public ContactMessage(String senderId, String receiverId, String name, String phone) {
        super(senderId, receiverId, name, MessageType.CONTACT);
        setMessageStatus(MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA);
        setLocalFile(phone);
    }

}