package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.messenger.Message;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message,MessageAdapter.MessageHolder> {
    private Context mContext;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, Context context) {
        super(options);
        this.mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        holder.messageTime.setText(model.getSent_timestamp()+"");
        TextView message = new TextView(mContext);
        message.setText(model.getMessage());
        holder.message_root.addView(message);
        ImageView message_status = new ImageView(mContext);
            // place read status on outgoing messages
            switch (model.getStatus()){
                case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET:
                    message_status.setImageResource(R.drawable.message_got_read_receipt_from_target);
                    break;
                case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                    message_status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                    break;
                case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER:
                    message_status.setImageResource(R.drawable.message_got_receipt_from_server);
                    break;
                case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                    message_status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                    break;
                case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET:
                    message_status.setImageResource(R.drawable.message_got_receipt_from_target);
                    break;
                case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                    message_status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                    break;
            }
            holder.message_root.addView(message_status);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text_message,viewGroup,false);
        return new MessageHolder(view);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        RelativeLayout message_root;
        TextView messageTime;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            message_root = (RelativeLayout) itemView.findViewById(R.id.message_root);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
        }
    }
}
