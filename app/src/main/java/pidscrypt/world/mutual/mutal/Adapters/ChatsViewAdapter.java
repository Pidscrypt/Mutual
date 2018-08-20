package pidscrypt.world.mutual.mutal.Adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pidscrypt.world.mutual.mutal.ChatActivity;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Friend;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.MessageType;

public class ChatsViewAdapter extends RecyclerView.Adapter<ChatsViewAdapter.ChatsViewHolder> {

    private List<Friend> chat_items;
    private Context mContext;

    public ChatsViewAdapter(List<Friend> chat_items, Context mContext) {
        this.chat_items = chat_items;
        this.mContext = mContext;
    }

    @Override
    public ChatsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.chat_item,viewGroup,false);
        ChatsViewHolder chatsViewHolder = new ChatsViewHolder(view);
        return chatsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatsViewHolder chatsViewHolder, int i) {
        Friend friend = chat_items.get(i);
        chatsViewHolder.friend_name.setText(friend.getName());
        chatsViewHolder.friend_recent_msg.setText(friend.getLastMsg());
        chatsViewHolder.last_msg_time.setText(friend.getLastSeen());
        //chatsViewHolder.num_unread_msg.setText(friend.getNumUnReadMsg());

        // place raad status on outgoing messages
        switch (friend.getLastMsgStatus()){
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET:
                chatsViewHolder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target);
                break;
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                chatsViewHolder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER:
                chatsViewHolder.message_status.setImageResource(R.drawable.message_got_receipt_from_server);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                chatsViewHolder.message_status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET:
                chatsViewHolder.message_status.setImageResource(R.drawable.message_got_receipt_from_target);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                chatsViewHolder.message_status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                break;
            default:
                    chatsViewHolder.message_status.setImageResource(R.drawable.message_unsent);
        }

        if(friend.getProfilePicUrl().equals("")){
            chatsViewHolder.friend_img.setImageResource(R.drawable.avatar_contact);
        }else{
            Glide.with(chatsViewHolder.friend_img.getContext()).load(friend.getProfilePicUrl()).into(chatsViewHolder.friend_img);
        }

        chatsViewHolder.friend_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"hello you pressed "+chatsViewHolder.friend_name.getText(),Toast.LENGTH_SHORT).show();
            }
        });

        chatsViewHolder.chatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = null;
                int[] screenLocation = new int[2];
                view.getLocationOnScreen(screenLocation);

                Intent chatActivity = new Intent(mContext,ChatActivity.class);
                int chatOrientation = mContext.getResources().getConfiguration().orientation;
                chatActivity.
                        putExtra("chat_name", chatsViewHolder.friend_name.getText())
                        .putExtra("chat_phone","479873498")
                        .putExtra("orientation",chatOrientation)
                        .putExtra("left",screenLocation[0])
                        .putExtra("top",screenLocation[1])
                        .putExtra("width",view.getWidth())
                        .putExtra("height",view.getHeight());


                b = ActivityOptions.makeScaleUpAnimation(view,0,0,view.getWidth(),view.getHeight()).toBundle();
                mContext.startActivity(chatActivity,b);
            }
        });
    }

    private void expandChatCardView(){
        //expand chat card when user clicks chat picture
    }

    @Override
    public int getItemCount() {
        return chat_items.size();
    }

    class ChatsViewHolder extends RecyclerView.ViewHolder {

        TextView friend_name, friend_recent_msg;
        ImageView friend_img;
        LinearLayout chatContainer;
        TextView last_msg_time;
        TextView num_unread_msg;
        ImageView message_status;

        public ChatsViewHolder(View itemView) {
            super(itemView);
            friend_name = (TextView) itemView.findViewById(R.id.friend_name);
            friend_recent_msg = (TextView) itemView.findViewById(R.id.friend_last_msg);
            friend_img = (CircleImageView) itemView.findViewById(R.id.friend_img);
            chatContainer = (LinearLayout) itemView.findViewById(R.id.chat_container);
            last_msg_time = (TextView) itemView.findViewById(R.id.last_msg_time);
            num_unread_msg = (TextView) itemView.findViewById(R.id.num_unread_msg);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);
        }
    }
}
