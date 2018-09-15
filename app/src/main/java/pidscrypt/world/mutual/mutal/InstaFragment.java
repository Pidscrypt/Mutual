package pidscrypt.world.mutual.mutal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pidscrypt.world.mutual.mutal.Adapters.ChatAdapter;
import pidscrypt.world.mutual.mutal.Adapters.FeedAdapter;
import pidscrypt.world.mutual.mutal.api.Conversation;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.Feed;
import pidscrypt.world.mutual.mutal.api.Tag;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class InstaFragment extends Fragment {

    private FloatingActionButton btn_add_feed;

    private RecyclerView feeds_recycler;
    private FirebaseUser firebaseUser;
    private FeedAdapter feedAdapter;
    private CollectionReference feedsRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();


    public InstaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_insta, container, false);
        btn_add_feed = layout.findViewById(R.id.btn_add_feed);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        feedsRef = db.collection(DatabaseNode.FEEDS);
        db.setFirestoreSettings(settings);
        setupFeeds(layout);

        btn_add_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MutualUser me = new MutualUser("test user", "testphone", "imagesuri", "sdj", "u9wsef9fbisgiybsibi", new Date().getTime(), false);
                List<String> images = new ArrayList<>();
                images.add("dgfgkjjdxfkd");
                images.add("reudoithsdhfjhidrl");

                Feed feed = new Feed(images, null, null, null, me);
                feedsRef.add(feed);
            }
        });

        return layout;
    }

    private void setupFeeds(View layout) {
        Query query = feedsRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(15);

        FirestoreRecyclerOptions<Feed> options = new FirestoreRecyclerOptions.Builder<Feed>().setQuery(query,Feed.class).build();

        feedAdapter = new FeedAdapter(options, getContext());

        final RecyclerView chatsRecycler = layout.findViewById(R.id.feeds_recycler);
        chatsRecycler.setHasFixedSize(true);

        chatsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatsRecycler.setAdapter(feedAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        feedAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        feedAdapter.stopListening();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
