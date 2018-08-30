package pidscrypt.world.mutual.mutal.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import pidscrypt.world.mutual.mutal.ChatsFragment;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Chat;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatMessage,ChatAdapter.ChatHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(FirestoreRecyclerOptions<ChatMessage> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(ChatHolder holder, int position, ChatMessage model) {
        holder.name.setText("dummy name");
        holder.numOfUnreadMessages.setText("2");
        holder.time.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));
        holder.message.setText(model.getMessage());

        switch(model.getMessageStatus()){
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET:
                holder.status.setImageResource(R.drawable.message_got_read_receipt_from_target);
                break;
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER:
                holder.status.setImageResource(R.drawable.message_got_receipt_from_server);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                holder.status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET:
                holder.status.setImageResource(R.drawable.message_got_receipt_from_target);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                break;
        }

        //if(model.getImage_uri().trim().equals("")){
            holder.image.setImageResource(R.drawable.avatar_contact);
        /*}else{
            Glide.with(holder.image.getContext()).load(model.getImage_uri()).into(holder.image);
        }*/

    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        //@TODO: update list
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
        Log.d("firestore ui error","there was an error");
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item,viewGroup,false);
        return new ChatHolder(view);
    }

    public void deleteChat(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ChatHolder extends RecyclerView.ViewHolder {

        private TextView name, numOfUnreadMessages, message, time;
        private ImageView status, image;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.friend_name);
            numOfUnreadMessages = (TextView) itemView.findViewById(R.id.num_unread_msg);
            message = (TextView) itemView.findViewById(R.id.friend_last_msg);
            time = (TextView) itemView.findViewById(R.id.last_msg_time);
            status = (ImageView) itemView.findViewById(R.id.message_status);
            image = (ImageView) itemView.findViewById(R.id.friend_img);
        }
    }
}
