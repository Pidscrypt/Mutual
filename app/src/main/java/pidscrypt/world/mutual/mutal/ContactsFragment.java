package pidscrypt.world.mutual.mutal;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import pidscrypt.world.mutual.mutal.Adapters.ContactsViewAdapter;
import pidscrypt.world.mutual.mutal.api.Contact;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.services.Contacts;
import pidscrypt.world.mutual.mutal.user.MutualUser;


public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnClickListener {


    private RecyclerView contacts_recycler;
    private List<Contact> contacts_list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef;
    private ContactsViewAdapter contactsViewAdapter;

    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
            android.R.id.text1
    };
    // Define variables for the contact the user selects
    // The contact's _ID value
    long mContactId;
    // The contact's LOOKUP_KEY
    String mContactKey;
    // A content URI for the selected contact
    Uri mContactUri;
    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the LOOKUP_KEY column
    private static final int LOOKUP_KEY_INDEX = 1;

    // Defines the text expression
    @SuppressLint("InlinedApi")
    private static final String SELECTION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    // Defines a variable for the search string
    private String mSearchString;
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = { mSearchString };

    private Contacts mContacts;

    public ContactsFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_contacts, container, false);
        contacts_recycler = layout.findViewById(R.id.contacts_recycler_view);
        userRef = db.collection(DatabaseNode.USERS);

        mContacts = new Contacts(getContext());

        /*ContactsViewAdapter contactsViewAdapter = new ContactsViewAdapter(mContacts.fetch(),getContext());
        contacts_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        contacts_recycler.setAdapter(contactsViewAdapter);*/

        setupContacts(layout);

        //contacts_recycler.setOnClickListener(this);

        return layout;
    }

    @SuppressLint("SetTextI18n")
    private void setupContacts(View view){
        Query query = userRef;

        FirestoreRecyclerOptions<MutualUser> options = new FirestoreRecyclerOptions.Builder<MutualUser>().setQuery(query,MutualUser.class).build();

        contactsViewAdapter = new ContactsViewAdapter(options, getContext());

        final RecyclerView contactsRecycler = view.findViewById(R.id.contacts_recycler_view);
        contactsRecycler.setHasFixedSize(true);

        contactsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsRecycler.setAdapter(contactsViewAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        contactsViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        contactsViewAdapter.stopListening();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(),"contact pressed",Toast.LENGTH_SHORT).show();
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

    /* inner classes */
}
