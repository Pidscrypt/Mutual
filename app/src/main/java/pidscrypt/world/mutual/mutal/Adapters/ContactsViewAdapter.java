package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pidscrypt.world.mutual.mutal.ChatActivity;
import pidscrypt.world.mutual.mutal.MyProfileActivity;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Contact;
import pidscrypt.world.mutual.mutal.services.Contacts;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class ContactsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Contact> contacts_list;
    private Context mContext;
    private static int CONTACT_VIEW = 1;
    private static int CONTACT_SEPERATOR = 2;

    public ContactsViewAdapter(Contacts contacts_list, Context mContext) {
        this.contacts_list = contacts_list.fetctContacts();
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if(viewType == CONTACT_VIEW){
            View view = inflater.inflate(R.layout.contact_list_item,viewGroup,false);
            return new ContactsViewHolder(view);
        }else{
            View view = inflater.inflate(R.layout.contacts_seperator,viewGroup,false);
            return new ContactsSeperatorViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder chatsViewHolder, int i) {
            if((getItem(i).isMutual() == 1) || (getItem(i).isMutual() == 2)){
                configureContactView((ContactsViewHolder) chatsViewHolder, i);
            } else{
                configureSeperatorContactView((ContactsSeperatorViewHolder) chatsViewHolder, i);
            }

    }

    private void configureSeperatorContactView(ContactsSeperatorViewHolder seperatorViewHolder, int i) {

    }

    private void configureContactView(ContactsViewHolder chatsViewHolder, int i){
        chatsViewHolder.contact_name.setText(contacts_list.get(i).getName());
        chatsViewHolder.contact_tag.setText(contacts_list.get(i).getNumber());
        chatsViewHolder.contact_img.setImageResource(contacts_list.get(i).getImg());
    }

    @Override
    public int getItemViewType(int position) {
        if((getItem(position).isMutual() == 1) || (getItem(position).isMutual() == 2)){
            return CONTACT_VIEW;
        }else{
            return CONTACT_SEPERATOR;
        }

    }

    @Override
    public int getItemCount() {
        return this.contacts_list.size();
    }

    private Contact getItem(int position){
        return contacts_list.get(position);
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView contact_name, contact_tag;
        ImageView contact_img;
        LinearLayout contact;

        ContactsViewHolder(View itemView) {
            super(itemView);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            contact_tag = (TextView) itemView.findViewById(R.id.contact_tag);
            contact_img = (CircleImageView) itemView.findViewById(R.id.contact_img);
            contact = (LinearLayout) itemView.findViewById(R.id.contact);
        }

    }

    class ContactsSeperatorViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout divider;

        ContactsSeperatorViewHolder(View itemView) {
            super(itemView);
            divider = (RelativeLayout) itemView.findViewById(R.id.divider);
        }

    }
}
