package pidscrypt.world.mutual.mutal.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
    public void onBindViewHolder(ContactsViewHolder chatsViewHolder, int i) {
        chatsViewHolder.contact_name.setText(contacts_list.get(i).getName());
        chatsViewHolder.contact_tag.setText(contacts_list.get(i).getTag());
        chatsViewHolder.contact_img.setImageResource(contacts_list.get(i).getImg());
    }

    @Override
    public int getItemCount() {
        return contacts_list.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contact_name, contact_tag;
        ImageView contact_img;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            contact_tag = (TextView) itemView.findViewById(R.id.contact_tag);
            contact_img = (CircleImageView) itemView.findViewById(R.id.contact_img);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
