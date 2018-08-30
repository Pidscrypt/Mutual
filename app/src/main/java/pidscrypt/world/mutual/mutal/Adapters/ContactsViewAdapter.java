package pidscrypt.world.mutual.mutal.Adapters;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pidscrypt.world.mutual.mutal.ChatActivity;
import pidscrypt.world.mutual.mutal.MyProfileActivity;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Contact;

public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ContactsViewHolder> {

    private List<Contact> contacts_list;
    private Context mContext;

    public ContactsViewAdapter(List<Contact> contact_items, Context mContext) {
        this.contacts_list = contact_items;
        this.mContext = mContext;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.contact_list_item,viewGroup,false);
        ContactsViewHolder chatsViewHolder = new ContactsViewHolder(view);
        return chatsViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactsViewHolder chatsViewHolder, int i) {
        chatsViewHolder.contact_name.setText(contacts_list.get(i).getName());
        chatsViewHolder.contact_tag.setText(contacts_list.get(i).getTag());
        if(contacts_list.get(i).getImage_uri().isEmpty()){
            chatsViewHolder.contact_img.setImageResource(contacts_list.get(i).getImg());
        }else{

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.avatar_contact)
                    .error(R.drawable.bg_outline_gray)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(contacts_list.get(i).getImage_uri()).thumbnail(0.5f).into(chatsViewHolder.contact_img);
        }

        chatsViewHolder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat_intent = new Intent(mContext, ChatActivity.class);
                Bundle b = new Bundle();
                b.putString("chat_name",chatsViewHolder.contact_name.getText().toString());
                b.putString("chat_phone",chatsViewHolder.contact_tag.getText().toString());
                b.putBoolean("from_contacts",true);
                chat_intent.putExtra("chat_details",b);
                mContext.startActivity(chat_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts_list.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contact_name, contact_tag;
        ImageView contact_img;
        LinearLayout contact;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            contact_tag = (TextView) itemView.findViewById(R.id.contact_tag);
            contact_img = (CircleImageView) itemView.findViewById(R.id.contact_img);
            contact = (LinearLayout) itemView.findViewById(R.id.contact);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
