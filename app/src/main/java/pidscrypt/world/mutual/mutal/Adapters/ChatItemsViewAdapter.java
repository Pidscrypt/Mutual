package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.MessageType;

public class ChatItemsViewAdapter extends RecyclerView.Adapter<ChatItemsViewAdapter.ChatItemsViewHolder> {

    private List<ChatMessage> chat_messages;
    private Context mContext;

    public ChatItemsViewAdapter(List<ChatMessage> chat_items, Context mContext) {
        this.chat_messages = chat_items;
        this.mContext = mContext;
    }

    @Override
    public ChatItemsViewHolder onCreateViewHolder(ViewGroup viewGroup,int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ChatItemsViewHolder chatItemsViewHolder = null;
        View view;
                view = inflater.inflate(
                        R.layout.item_text_message,
                        viewGroup,
                        false);
        chatItemsViewHolder = new ChatItemsViewHolder(view);
        return chatItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatItemsViewHolder chatItemsViewHolder, int i) {
        ChatMessage chatMessage = chat_messages.get(i);

        chatItemsViewHolder.message_root.setHorizontalGravity(chatMessage.getMessageGravity());
        chatItemsViewHolder.messageTime.setText(chatMessage.getMessage_time()+"");

        if(chatMessage.getMessageGravity() == Gravity.START){
            chatItemsViewHolder.message_root.setBackgroundResource(R.drawable.balloon_incoming_normal);
        }else{
            chatItemsViewHolder.message_root.setBackgroundResource(R.drawable.balloon_outgoing_normal);
            ImageView message_status = new ImageView(mContext);
            message_status.setMaxHeight(15);
            message_status.setMaxWidth(15);

            // place raad status on outgoing messages
            switch (chatMessage.getMessageStatus()){
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
                default:
                    if(chatMessage.getMessageType() != MessageType.TEXT){
                        message_status.setImageResource(R.drawable.message_unsent_onmedia);
                    }else{
                        message_status.setImageResource(R.drawable.message_unsent);
                    }
                    break;
            }
            chatItemsViewHolder.message_root.addView(message_status);
        }

        if(chatMessage.getMessageType() == MessageType.TEXT){
            TextView message = new TextView(mContext);
            message.setText(chatMessage.getMessage().toString());
            chatItemsViewHolder.message_root.addView(message);
        }else if (chatMessage.getMessageType() == MessageType.IMAGE){
            ImageView message = new ImageView(mContext);
            message.setScaleType(ImageView.ScaleType.CENTER_CROP);
            message.setAdjustViewBounds(true);
            if(!chatMessage.getMessage().equals("")){
                Glide.with(message.getContext()).load(chatMessage.getMessage()).into(message);
            }else{
                message.setImageResource(R.drawable.dami);
            }
            chatItemsViewHolder.message_root.addView(message);
        }
    }

    @Override
    public int getItemCount() {
        return chat_messages.size();
    }

    class ChatItemsViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout message_root;
        TextView messageTime;

        public ChatItemsViewHolder(View itemView) {
            super(itemView);

            message_root = (RelativeLayout) itemView.findViewById(R.id.message_root);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);

        }

    }
}
