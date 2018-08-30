package pidscrypt.world.mutual.mutal;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pidscrypt.world.mutual.mutal.Adapters.ChatMessagesViewAdapter;
import pidscrypt.world.mutual.mutal.api.Chat;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.ImageMessage;
import pidscrypt.world.mutual.mutal.api.MessageType;
import pidscrypt.world.mutual.mutal.api.TextMessage;
import pidscrypt.world.mutual.mutal.media.Audio;
import pidscrypt.world.mutual.mutal.messenger.Message;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class ChatActivity extends AppCompatActivity {

    private ImageView chat_img;
    private TextInputEditText text_msg;
    private static final int ANIM_DURATION = 500;
    private TextView name_chat;
    private ImageView send_message_btn, send_audio_btn;
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRefInChat = db.collection(DatabaseNode.MESSAGES);
    private CollectionReference usersRef = db.collection(DatabaseNode.USERS);
    private CollectionReference chatReference = db.collection(DatabaseNode.CHATS);
    private Animation anim;
    private Audio mAudioPlayer;
    private ChatMessagesViewAdapter chatItemsViewAdapter;
    private ImageView open_cam_btn;
    private StorageReference mStorage;
    //private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();

    String mCurrentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupWindowAnimations();
        //db.setFirestoreSettings(settings);

        text_msg = (TextInputEditText) findViewById(R.id.text_msg);
        name_chat = (TextView) findViewById(R.id.name_chat);
        send_message_btn = (ImageView) findViewById(R.id.send_message_btn);
        send_audio_btn = (ImageView) findViewById(R.id.send_audio_btn);

        open_cam_btn = (ImageView) findViewById(R.id.open_cam_btn);

        mStorage = FirebaseStorage.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Bundle b = bundle.getBundle("chat_details");
            name_chat.setText(b != null ? b.getString("chat_name") : b.getString("chat_phone"));
            /*if(Objects.requireNonNull(b).getBoolean("from_contacts")){
                List<MutualUser> participants = new ArrayList<>();
                final String[] myName = new String[1];
                final String[] myPhone = new String[1];
                final String[] otherOnesId = new String[1];
                DocumentReference userme = usersRef.document(FirebaseAuth.getInstance().getUid());
                userme.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       myName[0] = documentSnapshot.get("name").toString();
                       myPhone[0] = documentSnapshot.get("phone").toString();
                        Toast.makeText(ChatActivity.this,myPhone[0],Toast.LENGTH_SHORT).show();
                    }
                });
                Query otheruserquery = usersRef.whereEqualTo("phone",b.getString("chat_phone"));
                otheruserquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            otherOnesId[0] = task.getResult().getDocuments().get(0).getId();
                            Toast.makeText(ChatActivity.this,otherOnesId[0],Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ChatActivity.this,"You are offline",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                participants.add(new MutualUser(myName[0],myPhone[0],FirebaseAuth.getInstance().getUid()));
                participants.add(new MutualUser(name_chat.getText().toString(),b.getString("chat_phone"),otherOnesId[0]));
                Chat chat = new Chat("shdhjsad",participants);
                Map<String,Object> chats = new HashMap<>();
                chats.put("ghjhjcghx",chat);
                //chatReference.add(chat);
            }*/

        }

        //mToolbar.setLogo(R.drawable.avatar_contact);
        //mToolbar.setTitle("some name");
        loadMessages();

        text_msg.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(!s.toString().trim().isEmpty()){
                    send_audio_btn.setVisibility(View.GONE);
                    send_message_btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //status = "Typing"; // where status is a string you send to the other person
                if(s.toString().trim().isEmpty()){
                    send_audio_btn.setVisibility(View.VISIBLE);
                    send_message_btn.setVisibility(View.GONE);
                }
            }
        });

        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //@TODO: button send click actions
                sendMessage(MessageType.TEXT, null);
                text_msg.setText("");
                send_audio_btn.setVisibility(View.VISIBLE);
                send_message_btn.setVisibility(View.GONE);
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

        open_cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //takePhoto();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK){
            //sendMessage(MessageType.IMAGE, data);
            Uri imageUri = data.getData();
            final StorageReference filepath = mStorage.child("images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChatActivity.this,"upload done",Toast.LENGTH_SHORT).show();
                        sendMessage(MessageType.IMAGE, task);
                    }else{
                        Toast.makeText(ChatActivity.this,"upload failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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

        Query query = messageRefInChat.whereEqualTo("senderId",FirebaseAuth.getInstance().getUid()).orderBy("time_sent", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>().setQuery(query,ChatMessage.class).build();

        chatItemsViewAdapter = new ChatMessagesViewAdapter(options,ChatActivity.this);
        final RecyclerView chat_messages_recycler = (RecyclerView) findViewById(R.id.chat_messages);
        chat_messages_recycler.setHasFixedSize(true);
        chat_messages_recycler.setLayoutManager(new LinearLayoutManager(this));
        chat_messages_recycler.setAdapter(chatItemsViewAdapter);
    }

    private void sendMessage(int messageType, Task<UploadTask.TaskSnapshot> snapshot){
        ChatMessage chatMessage = null;

        switch(messageType){
            case MessageType.TEXT:
                String message = text_msg.getText().toString();
                if(message.trim().isEmpty()){
                    return;
                }
                chatMessage = new TextMessage(FirebaseAuth.getInstance().getUid(),"dhbxjhdgukgdzumsg",message);
                break;
            case MessageType.IMAGE:
                chatMessage = new ImageMessage(FirebaseAuth.getInstance().getUid(), "fedgjhbsjh",snapshot.getResult().getUploadSessionUri().toString());
                break;
        }
        CollectionReference messagesRef = FirebaseFirestore.getInstance().collection(DatabaseNode.MESSAGES);
        if (chatMessage != null) {
            messagesRef.add(chatMessage);
        }
    }


    public void takePhoto() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_" + ".jpeg";

            File mediaFile = createImageFile();
            // Uri    capturedImageUri =   FileProvider.getUriForFile(this, Utils.getMetaDataValue(this, MobiComKitConstants.PACKAGE_NAME) + ".provideridentifer", mediaFile);

            Uri capturedImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".mutual.main.provider", createImageFile());

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clip =
                        ClipData.newUri(getContentResolver(), "a Photo", capturedImageUri);

                cameraIntent.setClipData(clip);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                List<ResolveInfo> resInfoList =
                        getPackageManager()
                                .queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, capturedImageUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    grantUriPermission(packageName, capturedImageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }

            if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                if (mediaFile != null) {

                    startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "MT_" + timeStamp;
        File cacheDirectory = getBaseContext().getCacheDir();
        //  File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "Mutual/images/sent");

        File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "Mutual/images/sent");

        if (!cacheDir.exists())
            cacheDir.mkdirs();
        File image = new File(cacheDir + "/" + imageFileName + ".jpeg");

        mCurrentPhotoPath = image.getPath();
        return image;
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatItemsViewAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatItemsViewAdapter.stopListening();
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
