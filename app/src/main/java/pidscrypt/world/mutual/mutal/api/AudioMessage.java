package pidscrypt.world.mutual.mutal.api;

public class AudioMessage extends ChatMessage {



    public AudioMessage() {
        super();
    }

    public AudioMessage(String senderId, String receiverId, String imageUri, String localFile) {
        super(senderId, receiverId, imageUri, MessageType.AUDIO);
        setMessageStatus(MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA);
        setLocalFile(localFile);
    }

}