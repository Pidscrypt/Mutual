package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pidscrypt.world.mutual.mutal.ChatActivity;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Contact;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class MutualsViewAdapter extends FirestoreRecyclerAdapter<MutualUser,MutualsViewAdapter.ContactsViewHolder> {

    private List<Contact> contacts_list;
    private Context mContext;
    private FirebaseUser uid = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MutualsViewAdapter(@NonNull FirestoreRecyclerOptions<MutualUser> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull final MutualUser model) {
        if(!model.getUId().equals(uid.getUid())){
            holder.contact_name.setText(model.getName());
            holder.contact_tag.setText(model.getPhone());
            if(model.getImage_uri().trim().isEmpty()){
                holder.contact_img.setImageResource(R.drawable.avatar_contact);
            }else{

                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.avatar_contact)
                        .error(R.drawable.avatar_contact)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getImage_uri()).thumbnail(0.5f).into(holder.contact_img);
            }

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat_intent = new Intent(mContext, ChatActivity.class);
                Bundle b = new Bundle();
                b.putString("chat_name",model.getName());
                b.putString("chat_phone",model.getPhone());
                b.putString("last_seen", String.valueOf(model.getLast_seen()));
                b.putString("image_uri", model.getImage_uri());
                b.putString("uid",model.getUId());
                b.putBoolean("isOnline", model.isOnline());
                b.putBoolean("from_contacts",true);
                chat_intent.putExtra("chat_details",b);
                mContext.startActivity(chat_intent);
            }
        });
        }

    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list_item,viewGroup,false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
