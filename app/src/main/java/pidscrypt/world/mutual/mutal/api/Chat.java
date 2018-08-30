package pidscrypt.world.mutual.mutal.api;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

import pidscrypt.world.mutual.mutal.user.MutualUser;

public class Chat {
    private String chat_id;
    private List<ChatMessage> messages;
    private List<MutualUser> participants;
    private ChatMessage last_message;

    public Chat() {
    }

    public Chat(String chat_id, List<MutualUser> participants) {
        this.chat_id = chat_id;
        this.participants = participants;
    }

    public String getChat_id() {
        return chat_id;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public List<MutualUser> getParticipants() {
        return participants;
    }

    public ChatMessage getLast_message() {
        return last_message;
    }
}
