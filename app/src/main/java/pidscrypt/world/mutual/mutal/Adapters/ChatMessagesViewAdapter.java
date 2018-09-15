package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.MessageType;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.api.MutualMessageViewTypes;

public class ChatMessagesViewAdapter extends FirestoreRecyclerAdapter<ChatMessage,RecyclerView.ViewHolder> {

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
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull ChatMessage model) {

        if(TextUtils.equals(model.getSenderId(), FirebaseAuth.getInstance().getUid())){
            switch(model.getMessageType()){
                case MessageType.TEXT:
                    configureSentMessageViewHolder((ChatMessageViewHolder) holder, position, model);
                    break;
                case MessageType.IMAGE:
                    configureSentImageViewHolder((ChatImageOutgoingViewHolder) holder, position, model);
                    break;
            }

        }else{
            switch(model.getMessageType()){
                case MessageType.TEXT:
                    configureRecievedMessageViewHolder((OtherChatMessageViewHolder) holder, position, model);
                    break;
                case MessageType.IMAGE:
                    configureRecievedImageViewHolder((ChatImageIncomingViewHolder) holder, position, model);
                    break;
            }

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text_message,viewGroup,false);
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case MutualMessageViewTypes.TEXT_MESSAGE_OUTGOING:
                View viewTextIn = layoutInflater.inflate(R.layout.item_messsage_outgoing, viewGroup, false);
                viewHolder = new ChatMessageViewHolder(viewTextIn);
                break;
            case MutualMessageViewTypes.TEXT_MESSAGE_INCOMING:
                View viewTextOut = layoutInflater.inflate(R.layout.item_messsage_incoming, viewGroup, false);
                viewHolder = new OtherChatMessageViewHolder(viewTextOut);
                break;
            case MutualMessageViewTypes.IMAGE_MESSAGE_INCOMING:
                View viewImageIn = layoutInflater.inflate(R.layout.item_image_messsage_incoming, viewGroup, false);
                viewHolder = new ChatImageIncomingViewHolder(viewImageIn);
                break;
            case MutualMessageViewTypes.IMAGE_MESSAGE_OUTGOING:
                View viewImageOut = layoutInflater.inflate(R.layout.item_image_messsage_outgoing, viewGroup, false);
                viewHolder = new ChatImageOutgoingViewHolder(viewImageOut);
                break;
        }
        return viewHolder;
        //return new ChatMessageViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (TextUtils.equals(getItem(position).getSenderId(), FirebaseAuth.getInstance().getUid())) {
            switch (getItem(position).getMessageType()){
                case MessageType.TEXT:
                    type = MutualMessageViewTypes.TEXT_MESSAGE_OUTGOING;
                    break;
                case MessageType.IMAGE:
                    type = MutualMessageViewTypes.IMAGE_MESSAGE_OUTGOING;
            }
        } else {
            switch (getItem(position).getMessageType()){
                case MessageType.TEXT:
                     type = MutualMessageViewTypes.TEXT_MESSAGE_INCOMING;
                     break;
                case MessageType.IMAGE:
                     type = MutualMessageViewTypes.IMAGE_MESSAGE_INCOMING;
            }
        }
        return type;
    }



    private void configureRecievedMessageViewHolder(OtherChatMessageViewHolder holder, int position, @NonNull ChatMessage model){
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));
        switch (model.getMessageType()){
            case MessageType.TEXT:
                TextView message = new TextView(mContext);
                message.setText(model.getMessage());
                holder.message.addView(message);
                break;
            case MessageType.IMAGE:
                ImageView image_message = new ImageView(mContext);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.avatar_contact)
                        .error(R.drawable.bg_outline_gray)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getMessage()).thumbnail(0.5f).into(image_message);
                holder.message.addView(image_message);
                break;
        }
    }

    private void configureSentImageViewHolder(ChatImageOutgoingViewHolder holder, int position, @NonNull ChatMessage model){
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));

        switch (model.getMessageStatus()){
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                break;
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.avatar_contact)
                .error(R.drawable.bg_outline_gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getMessage()).thumbnail(0.5f).into(holder.message);
    }

    private void configureRecievedImageViewHolder(ChatImageIncomingViewHolder holder, int position, @NonNull ChatMessage model){
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.avatar_contact)
                .error(R.drawable.bg_outline_gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getMessage()).thumbnail(0.5f).into(holder.message);
    }

    private void configureSentMessageViewHolder(ChatMessageViewHolder holder, int position, @NonNull ChatMessage model){

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
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.avatar_contact)
                        .error(R.drawable.bg_outline_gray)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getMessage()).thumbnail(0.5f).into(image_message);
                holder.message.addView(image_message);
                break;
        }
    }


    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout message;
        TextView messageTime;
        ImageView message_status;

        ChatMessageViewHolder(View itemView) {
            super(itemView);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (RelativeLayout) itemView.findViewById(R.id.message);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);

        }

    }

    class OtherChatMessageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout message;
        TextView messageTime;

        OtherChatMessageViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (RelativeLayout) itemView.findViewById(R.id.message);

        }

    }

    class ChatImageOutgoingViewHolder extends RecyclerView.ViewHolder {

        ImageView message;
        TextView messageTime;
        ImageView message_status;

        ChatImageOutgoingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (ImageView) itemView.findViewById(R.id.message);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);

        }

    }

    class ChatImageIncomingViewHolder extends RecyclerView.ViewHolder {

        ImageView message;
        TextView messageTime;

        ChatImageIncomingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (ImageView) itemView.findViewById(R.id.message);

        }

    }
}
