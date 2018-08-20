package pidscrypt.world.mutual.mutal;

import android.animation.TimeInterpolator;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pidscrypt.world.mutual.mutal.Adapters.ChatItemsViewAdapter;
import pidscrypt.world.mutual.mutal.Adapters.ChatsViewAdapter;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.Friend;
import pidscrypt.world.mutual.mutal.api.ImageMessage;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.TextMessage;

public class ChatActivity extends AppCompatActivity {

    private ImageView chat_img;
    private TextInputEditText text_msg;
    private static final int ANIM_DURATION = 500;
    private TextView name_chat;
    private ImageView send_message_btn;
    private RecyclerView chat_messages_recycler;
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text_msg = (TextInputEditText) findViewById(R.id.text_msg);
        name_chat = (TextView) findViewById(R.id.name_chat);
        send_message_btn = (ImageView) findViewById(R.id.send_message_btn);
        chat_messages_recycler = (RecyclerView) findViewById(R.id.chat_messages);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            name_chat.setText(bundle.getString("chat_name"));
        }

        //mToolbar.setLogo(R.drawable.avatar_contact);
        mToolbar.setTitle(getIntent().getStringExtra("chat_name"));

        ChatItemsViewAdapter chatItemsViewAdapter = new ChatItemsViewAdapter(getChatItems(),ChatActivity.this);
        chat_messages_recycler.setLayoutManager(new LinearLayoutManager(this));
        chat_messages_recycler.setAdapter(chatItemsViewAdapter);

        text_msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                /*if(keyEvent.getCharacters() != null){

                }*/
                return false;
            }
        });

        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nickson();
                //@TODO: button send click actions
            }
        });

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
