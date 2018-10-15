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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pidscrypt.world.mutual.mutal.Database.MutualDB;

import static java.lang.Thread.sleep;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Welcome extends AppCompatActivity {

    private LinearLayout welcome_reveal;
    private ImageView logo;
    private LinearLayout logo_container;
    private Button btn_continue;
    private Handler handler;
    private Runnable runnable;
    private static final int SPLASH_TIME = 2000;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthState;

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

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        welcome_reveal = (LinearLayout)findViewById(R.id.moving_bg);
        //btn_continue = (Button) findViewById(R.id.btn_continue);
        logo = findViewById(R.id.logo);
        logo_container = (LinearLayout) findViewById(R.id.logo_container);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                firebaseAuth = FirebaseAuth.getInstance();
                        //FireBaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();


                        if(user == null){
                            PhoneAuthActivity.startIntent(Welcome.this, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }else{
                            LandingActivity.startActivity(Welcome.this, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }

                        handler.sendEmptyMessage(0);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.fui_slide_out_left);
                        finish();

            }
        };
        startAnimation();
        handler.postDelayed(runnable, SPLASH_TIME);
/*
        Thread chooseWelcomeThread = new Thread(runnable);
        startAnimation();
        chooseWelcomeThread.start();*/




        //startAnimation();

        /*btn_continue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Welcome.this,PhoneAuthActivity.class);
                startActivity(i);
                Welcome.this.finish();
            }
        });*/

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

}
