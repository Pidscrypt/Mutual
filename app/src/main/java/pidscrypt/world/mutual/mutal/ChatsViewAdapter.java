package pidscrypt.world.mutual.mutal;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pidscrypt.world.mutual.mutal.api.Friend;

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
        chatsViewHolder.friend_name.setText(chat_items.get(i).getName());
        chatsViewHolder.friend_recent_msg.setText(chat_items.get(i).getLastMsg());
        chatsViewHolder.friend_img.setImageResource(chat_items.get(i).getPhoto());

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

        public ChatsViewHolder(View itemView) {
            super(itemView);
            friend_name = (TextView) itemView.findViewById(R.id.friend_name);
            friend_recent_msg = (TextView) itemView.findViewById(R.id.friend_last_msg);
            friend_img = (CircleImageView) itemView.findViewById(R.id.friend_img);
            chatContainer = (LinearLayout) itemView.findViewById(R.id.chat_container);
        }
    }
}
