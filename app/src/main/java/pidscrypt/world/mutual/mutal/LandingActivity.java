package pidscrypt.world.mutual.mutal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import io.fabric.sdk.android.Fabric;
import pidscrypt.world.mutual.mutal.Adapters.LandingPagerAdapter;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.UserStatus;

public class LandingActivity extends AppCompatActivity implements ChatsFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener, MutualsFragment.OnFragmentInteractionListener, InstaFragment.OnFragmentInteractionListener {

    private LandingPagerAdapter landingPagerAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthState;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mUserDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics()); 
        setContentView(R.layout.activity_landing);
        setupWindowAnimations();

        landingPagerAdapter = new LandingPagerAdapter(getSupportFragmentManager());
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(landingPagerAdapter);
        mTabLayout.setTabsFromPagerAdapter(landingPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(3).setIcon(R.drawable.ic_group);
        //mTabLayout.getTabAt(0).setIcon(R.drawable.ic_contact_mail);

        mViewPager.setCurrentItem(1,true);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Explode explodeLOLLIPOP = new Explode();
            explodeLOLLIPOP.setDuration(1000);
            getWindow().setEnterTransition(explodeLOLLIPOP);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth.addAuthStateListener(firebaseAuthState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            mUserDocRef = db.collection(DatabaseNode.USERS).document(user.getUid());
            mUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        mUserDocRef.update("online", UserStatus.ONLINE);
                    }
                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //firebaseAuth.addAuthStateListener(firebaseAuthState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            mUserDocRef.update("online", UserStatus.OFFLINE);
            mUserDocRef.update("last_seen", new Date().getTime());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                Intent settings_activity = new Intent(LandingActivity.this, MyProfileActivity.class);
                startActivity(settings_activity);
                break;
            case R.id.action_gallery:
                Intent gallery_activity = new Intent(LandingActivity.this, Gallery.class);
                startActivity(gallery_activity);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static void startActivity(Context context, int flags){
        Intent intent = new Intent(context, LandingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }
}
