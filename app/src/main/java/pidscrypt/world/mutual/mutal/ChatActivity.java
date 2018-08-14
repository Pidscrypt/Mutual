package pidscrypt.world.mutual.mutal;

import android.animation.TimeInterpolator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private ImageView chat_img;
    private static final int ANIM_DURATION = 500;
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        //mToolbar.setLogo(R.drawable.avatar_contact);
        mToolbar.setTitle(getIntent().getStringExtra("chat_name"));
    }

    @Override
    public void onBackPressed() {
        startExitAnimation(new Runnable(){
            @Override
            public void run() {
                finish();
            }
        });
    }

    private void startExitAnimation(Runnable runnable) {
        Bundle animBunddle;

        runnable.run();

        //final long animDuration = (long) (ANIM_DURATION * ActivityAnimations)
        //@TODO: code exit animations.... exists into chat card...
    }

    @Override
    public void finish() {
        super.finish();
        
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_chat,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.action_media:
                Intent mediaActivity = new Intent(ChatActivity.this,ChatMediaActivity.class);
                startActivity(mediaActivity);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
