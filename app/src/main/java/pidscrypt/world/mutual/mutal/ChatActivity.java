package pidscrypt.world.mutual.mutal;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pidscrypt.world.mutual.mutal.Adapters.ChatItemsViewAdapter;
import pidscrypt.world.mutual.mutal.Adapters.ChatsViewAdapter;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.Friend;
import pidscrypt.world.mutual.mutal.api.ImageMessage;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.TextMessage;
import pidscrypt.world.mutual.mutal.media.Audio;

public class ChatActivity extends AppCompatActivity {

    private ImageView chat_img;
    private TextInputEditText text_msg;
    private static final int ANIM_DURATION = 500;
    private TextView name_chat;
    private ImageView send_message_btn, send_audio_btn;
    private RecyclerView chat_messages_recycler;
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRefInChat = db.collection(DatabaseNode.MESSAGES);
    private Animation anim;
    private Audio mAudioPlayer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupWindowAnimations();

        text_msg = (TextInputEditText) findViewById(R.id.text_msg);
        name_chat = (TextView) findViewById(R.id.name_chat);
        send_message_btn = (ImageView) findViewById(R.id.send_message_btn);
        send_audio_btn = (ImageView) findViewById(R.id.send_audio_btn);
        chat_messages_recycler = (RecyclerView) findViewById(R.id.chat_messages);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            name_chat.setText("some name");
        }

        //mToolbar.setLogo(R.drawable.avatar_contact);
        mToolbar.setTitle("some name");

        ChatItemsViewAdapter chatItemsViewAdapter = new ChatItemsViewAdapter(getChatItems(),ChatActivity.this);
        chat_messages_recycler.setLayoutManager(new LinearLayoutManager(this));
        chat_messages_recycler.setAdapter(chatItemsViewAdapter);

        text_msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {


                if(keyEvent.getCharacters() != null){
                    switchToText(true);
                }else{
                    switchToText(false);
                }
                return false;
            }
        });

        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nickson();
                //@TODO: button send click actions
                text_msg.setText("");
            }
        });

        send_audio_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scaleout);
                        anim.setInterpolator(new OvershootInterpolator());
                        send_audio_btn.startAnimation(anim);

                        mAudioPlayer = new Audio();
                        mAudioPlayer.startRecording();
                        break;
                    case MotionEvent.ACTION_UP:
                        mAudioPlayer.stopRecording();
                        mAudioPlayer.fileExists();

                        //@TODO: send audio message
                        break;
                    case MotionEvent.ACTION_MOVE:
                        /*if(MotionEvent.obtain(200,2000,MotionEvent.ACTION_UP,0,30)){

                        }*/

                        //@TODO: Listen for moving finger on screen to terminate recording ...
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Toolbar","Clicked");
            }
        });

    }




    private void setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideLOLLIPOP = new Slide();
            slideLOLLIPOP.setDuration(1000);
            getWindow().setExitTransition(slideLOLLIPOP);
        }
    }

    private void switchToText(boolean toText){
        anim = AnimationUtils.loadAnimation(this,R.anim.slide_in_up);
        if(toText){
            anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
            anim.reset();
            send_audio_btn.clearAnimation();
            send_audio_btn.startAnimation(anim);
        }else{
            anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
            anim.reset();
            send_message_btn.clearAnimation();
            send_message_btn.startAnimation(anim);
        }
    }

    private void loadMessages(){
        Query query = messageRefInChat.whereEqualTo("by_uid",FirebaseAuth.getInstance().getUid());
    }

    private List<ChatMessage> getChatItems() {
        List<ChatMessage> list = new ArrayList<>();
        String UID = FirebaseAuth.getInstance().getUid();
        // add chats from firebase user list
        list.add(new TextMessage(UID,"hello man!",255467, MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET));
        list.add(new TextMessage(UID,"do you mind another drink?",25346,MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET));
        list.add(new TextMessage("euayb43q778HJgjdh","I`d like to go out with you some time.",346858, MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER));
        list.add(new TextMessage(UID,"Well, to day is a very good day.",4665465,MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER));
        list.add(new ImageMessage(UID,"",4665465,MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER));

        return list;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
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
