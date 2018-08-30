package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.MessageType;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;

public class ChatMessagesViewAdapter extends FirestoreRecyclerAdapter<ChatMessage,ChatMessagesViewAdapter.ChatMessageViewHolder> {

    private Context mContext;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options query options
     */
    public ChatMessagesViewAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options, Context context) {
        super(options);
        this.mContext = context;
    }




    @Override
    protected void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position, @NonNull ChatMessage model) {

        if(model.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            holder.message_root.setBackground(mContext.getResources().getDrawable(R.drawable.balloon_outgoing_normal));
            holder.message_root.setHorizontalGravity(Gravity.END);
        }else{
            holder.message_root.setBackground(mContext.getResources().getDrawable(R.drawable.balloon_incoming_normal));
            holder.message_root.setHorizontalGravity(Gravity.START);
        }

        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));
        switch (model.getMessageStatus()){
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_target);
                break;
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET:
                holder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_server);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                break;
        }

        switch (model.getMessageType()){
            case MessageType.TEXT:
                TextView message = new TextView(mContext);
                message.setText(model.getMessage());
                holder.message.addView(message);
                break;
            case MessageType.IMAGE:
                ImageView image_message = new ImageView(mContext);
                Glide.with(mContext).load(model.getMessage()).thumbnail(0.5f).into(image_message);
                holder.message.addView(image_message);
                break;
        }
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text_message,viewGroup,false);
        return new ChatMessageViewHolder(view);
    }


    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout message_root,message;
        TextView messageTime;
        ImageView message_status;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);

            message_root = (RelativeLayout) itemView.findViewById(R.id.message_root);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (RelativeLayout) itemView.findViewById(R.id.message);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);

        }

    }
}
