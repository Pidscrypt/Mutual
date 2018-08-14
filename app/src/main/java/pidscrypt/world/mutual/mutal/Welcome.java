package pidscrypt.world.mutual.mutal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import pidscrypt.world.mutual.mutal.Database.MutualDB;

import static java.lang.Thread.sleep;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Welcome extends AppCompatActivity {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private LinearLayout welcome_reveal;
    private ImageView logo;
    private LinearLayout logo_container;
    private ViewGroup mSceneRoot;
    private TransitionManager transitionManager;
    private Button btn_continue,go_home, sign_up_btn;

    private boolean mVisible;

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

                Snackbar.make(view,"Press continue! have Fun!",Snackbar.LENGTH_SHORT).show();
            return false;
        }
    };

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        setupWindowAnimations();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        welcome_reveal = (LinearLayout)findViewById(R.id.moving_bg);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        logo = findViewById(R.id.logo);
        logo_container = (LinearLayout) findViewById(R.id.logo_container);

startAnimation();
        mVisible = true;

        //createDatabase();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Welcome.this,PhoneAuthActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //Welcome.this.finish();
            }
        });

    }

    public void createDatabase(){
        MutualDB mdb = new MutualDB();
        mdb.setDatabaseName("testbasemutual");

        try{
            mdb.CreateTables();
        }catch (Exception ex){

        }

    }

    private void setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideLOLLIPOP = new Slide();
            slideLOLLIPOP.setDuration(1000);
            getWindow().setExitTransition(slideLOLLIPOP);
        }
    }

    public void startAnimation(){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.slide_in_up);
        anim.reset();
        welcome_reveal.clearAnimation();
        welcome_reveal.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        logo.clearAnimation();
        logo.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this,R.anim.slide_in_down);
        anim.reset();
        logo_container.clearAnimation();
        logo_container.startAnimation(anim);
    }

    private void toggle(View view) {
        if (mVisible) {
            hide(view);
        } else {
            show(view);
        }
    }

    private void hide(View view) {
        mVisible = false;
        view.setVisibility(View.INVISIBLE);
        view.animate();
    }

    @SuppressLint("InlinedApi")
    private void show(View view) {
        mVisible = true;
        view.setVisibility(View.VISIBLE);
        view.animate();
    }
}
