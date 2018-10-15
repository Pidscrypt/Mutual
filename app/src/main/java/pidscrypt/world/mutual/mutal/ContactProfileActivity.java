package pidscrypt.world.mutual.mutal;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.renderscript.ScriptIntrinsicLUT;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import pidscrypt.world.mutual.mutal.Adapters.ContactProfilePagerAdapter;

public class ContactProfileActivity extends AppCompatActivity {

    private LinearLayout content_container;
    private ScrollView scrollView;
    private ImageView status_img;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        //getWindow().setAllowEnterTransitionOverlap(false);

        ContactProfilePagerAdapter contactProfilePagerAdapter = new ContactProfilePagerAdapter(getSupportFragmentManager());
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        content_container = (LinearLayout) findViewById(R.id.content_container);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        status_img = (ImageView) findViewById(R.id.status_img);

        mViewPager.setAdapter(contactProfilePagerAdapter);
        mTabLayout.setTabsFromPagerAdapter(contactProfilePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                /*float y, scrollY, scale;
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        y = motionEvent.getY();
                        scale = y/100;
                        scrollY = view.getScrollY();
                        status_img.setScaleX(scale);
                        Toast.makeText(ContactProfileActivity.this, String.valueOf(y), Toast.LENGTH_SHORT).show();
                        break;
                }*/
                return false;
            }
        });

    }

}
