package pidscrypt.world.mutual.mutal;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import pidscrypt.world.mutual.mutal.Adapters.ContactsViewAdapter;
import pidscrypt.world.mutual.mutal.services.Contacts;

public class SendMessageToActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnClickListener {
    ContactsViewAdapter adapter;
    RecyclerView contacts_recycler;
    private Contacts mContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_send_message_to);
        setContentView(R.layout.fragment_contacts_new_message);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Send to ...");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contacts_recycler = findViewById(R.id.contacts_recycler_view);

        mContacts = new Contacts(this);

        adapter = new ContactsViewAdapter(mContacts.fetch(),this);
        contacts_recycler.setLayoutManager(new LinearLayoutManager(this));
        contacts_recycler.setAdapter(adapter);
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

    }
}
