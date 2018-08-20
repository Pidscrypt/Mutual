package pidscrypt.world.mutual.mutal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pidscrypt.world.mutual.mutal.Adapters.LandingPagerAdapter;

public class LandingActivity extends AppCompatActivity implements ChatsFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener, MutualsFragment.OnFragmentInteractionListener, InstaFragment.OnFragmentInteractionListener {

    private LandingPagerAdapter landingPagerAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_contact_mail);

        mViewPager.setCurrentItem(1,true);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthState = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                //FireBaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    Intent welcomeScreen = new Intent(LandingActivity.this, Welcome.class);
                    welcomeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(welcomeScreen);
                }
            }
        };

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
        firebaseAuth.addAuthStateListener(firebaseAuthState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(firebaseAuthState);
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
                Intent settings_activity = new Intent(LandingActivity.this, SettingsActivity.class);
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
}
