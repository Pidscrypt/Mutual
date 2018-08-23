package pidscrypt.world.mutual.mutal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import pidscrypt.world.mutual.mutal.Adapters.ChatAdapter;
import pidscrypt.world.mutual.mutal.Adapters.ChatsViewAdapter;
import pidscrypt.world.mutual.mutal.Database.Database;
import pidscrypt.world.mutual.mutal.api.Chat;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.Friend;
import pidscrypt.world.mutual.mutal.api.MessageStatus;

public class ChatsFragment extends Fragment {

    private RecyclerView chats_recycler;
    private List<Friend> friend_list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messagesRef = db.collection(DatabaseNode.MESSAGES);
    private ChatAdapter chatAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_chats, container, false);
        chats_recycler = layout.findViewById(R.id.chats_recycler);
/*
        //ChatsViewAdapter chatsViewAdapter = new ChatsViewAdapter(getChats(),getContext());
        ChatAdapter chatAdapter = new ChatAdapter(getChatsFromFirestore(),getContext());
        chats_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        chats_recycler.setAdapter(chatAdapter);*/

        setupChats(layout);

        return layout;
    }

    private void setupChats(View view){
        Query query = messagesRef.orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>().setQuery(query,Chat.class).build();
        chatAdapter = new ChatAdapter(options);

        RecyclerView chatsRecycler = view.findViewById(R.id.chats_recycler);
        chatsRecycler.setHasFixedSize(true);

        chatsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatsRecycler.setAdapter(chatAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }

    private List<Friend> getChats(){
        List<Friend> list = new ArrayList<>();



        // add chats from firebase user list

        list.add(new Friend("jshgjdsdffzhbs","where are you man?","","+256773891234", MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET));
        list.add(new Friend("uhbziu47bkbcDS","Daniel!!","","+256705056872",MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER));
        list.add(new Friend("ajgjbgzjhgjsh","I need that php API up tomorrow. BTW the friday meeting is still on","","+256773891234",MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER));
        list.add(new Friend("khkzhdkjhfkdsjhk","can we talk?","","+256773891234",MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET));
        list.add(new Friend("ukdhzjhdzkhkjdz","Up in lab","","+256773891234",MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET));

        return list;
    }

    private List<Chat> getChatsFromFirestore(){

        List<Chat> list = new ArrayList<>();
        return list;
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
