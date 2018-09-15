package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;

import pidscrypt.world.mutual.mutal.ChatActivity;
import pidscrypt.world.mutual.mutal.ChatsFragment;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Chat;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.Contact;
import pidscrypt.world.mutual.mutal.api.Conversation;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.services.Contacts;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class ChatAdapter extends FirestoreRecyclerAdapter<Conversation,ChatAdapter.ChatHolder> {

    private Context mContext;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(FirestoreRecyclerOptions<Conversation> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatHolder holder, int position, @NonNull final Conversation model) {
        //final String list_user_id = getSnapshots().get(position).;
        Contacts phoneContacts = new Contacts(mContext);

        holder.name.setText(model.getWith());
        //@TODO: fix contact names
        /*for (Contact phoneContact:
             phoneContacts.fetch()) {
            if(phoneContact.getNumber().equals(model.getWith())){
                holder.name.setText(phoneContact.getName());
                break;
            }
        }*/

        if(model.getCount() < 1){
            holder.numOfUnreadMessages.setVisibility(View.INVISIBLE);
        }else{
            holder.numOfUnreadMessages.setText(String.valueOf(model.getCount()));
        }

        //MutualDateFormat mTimeAgo = new MutualDateFormat();
        //long lastTime = Long.parseLong(model.getTimestamp());
        holder.time.setText(MutualDateFormat.getTimeAgo(model.getTimestamp(),mContext));
        //holder.time.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTimestamp()));
        holder.message.setText(model.getLastMsg());

        switch(model.getLast_msg_status()){
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

        if(model.getImg_uri().trim().isEmpty()){
            holder.image.setImageResource(R.drawable.avatar_contact);
        }else{

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.avatar_contact)
                    .error(R.drawable.avatar_contact)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getImg_uri()).thumbnail(0.5f).into(holder.image);
        }

        holder.chat_none_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject user;

                Intent chat_intent = new Intent(mContext, ChatActivity.class);
                Bundle b = new Bundle();
                b.putString("chat_name",holder.name.getText().toString());
                b.putString("chat_phone",model.getWith());
                b.putString("image_uri", model.getImg_uri());
                try {
                    user = new JSONObject(model.getUser().toString());
                    b.putString("uid",user.getString("uid"));
                    Toast.makeText(mContext, user.toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                b.putBoolean("from_contacts",true);
                chat_intent.putExtra("chat_details",b);
                mContext.startActivity(chat_intent);
            }
        });

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
        /*switch (i){
            case 1:
                break;
            case 2:
                break;
        }*/
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item,viewGroup,false);
        return new ChatHolder(view);
    }

    public void deleteChat(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ChatHolder extends RecyclerView.ViewHolder {

        private TextView name, numOfUnreadMessages, message, time;
        private ImageView status, image;
        private LinearLayout chat_none_image;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.friend_name);
            numOfUnreadMessages = (TextView) itemView.findViewById(R.id.num_unread_msg);
            message = (TextView) itemView.findViewById(R.id.friend_last_msg);
            time = (TextView) itemView.findViewById(R.id.last_msg_time);
            status = (ImageView) itemView.findViewById(R.id.message_status);
            image = (ImageView) itemView.findViewById(R.id.friend_img);
            chat_none_image = (LinearLayout) itemView.findViewById(R.id.chat_none_image);
        }

        public ChatHolder(View itemView, boolean mine){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.friend_name);
            numOfUnreadMessages = (TextView) itemView.findViewById(R.id.num_unread_msg);
            message = (TextView) itemView.findViewById(R.id.friend_last_msg);
            time = (TextView) itemView.findViewById(R.id.last_msg_time);
            status = (ImageView) itemView.findViewById(R.id.message_status);
            image = (ImageView) itemView.findViewById(R.id.friend_img);
            chat_none_image = (LinearLayout) itemView.findViewById(R.id.chat_none_image);
        }
    }
}
