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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import pidscrypt.world.mutual.mutal.ChatActivity;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Conversation;
import pidscrypt.world.mutual.mutal.api.Feed;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.services.Contacts;

public class FeedAdapter extends FirestoreRecyclerAdapter<Feed,FeedAdapter.FeedHolder> {

    private Context mContext;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeedAdapter(FirestoreRecyclerOptions<Feed> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final FeedHolder holder, int position, @NonNull final Feed model) {
        //holder.user_name.setText(model.getFeed_owner().getName());
        holder.user_name.setText("test user");

    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        //@TODO: update list
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
    }

    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item,viewGroup,false);
        return new FeedHolder(view);
    }

    class FeedHolder extends RecyclerView.ViewHolder {

        private TextView user_name;
        private ImageView image;

        FeedHolder(@NonNull View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }
}
