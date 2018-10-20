package pidscrypt.world.mutual.mutal;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pidscrypt.world.mutual.mutal.Adapters.ChatMessagesViewAdapter;
import pidscrypt.world.mutual.mutal.api.AudioMessage;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.ContactMessage;
import pidscrypt.world.mutual.mutal.api.Conversation;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.DocumentMessage;
import pidscrypt.world.mutual.mutal.api.ImageMessage;
import pidscrypt.world.mutual.mutal.api.MediaPath;
import pidscrypt.world.mutual.mutal.api.MessageType;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.api.Notification;
import pidscrypt.world.mutual.mutal.api.TextMessage;
import pidscrypt.world.mutual.mutal.constants.MediaAction;
import pidscrypt.world.mutual.mutal.media.Audio;
import pidscrypt.world.mutual.mutal.media.Media;
import pidscrypt.world.mutual.mutal.media.MediaUpload;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView user_img;
    private TextInputEditText text_msg;
    private static final int ANIM_DURATION = 500;
    private TextView name_chat, lastSeen;
    private ImageView send_message_btn, send_audio_btn, stop_audio_btn;
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRefInChat;
    private CollectionReference usersRef;
    private DocumentReference chatReference;
    private DocumentReference OtherChatReference;
    private Animation anim;
    private Audio mAudioPlayer;
    private ChatMessagesViewAdapter chatItemsViewAdapter;
    RecyclerView chat_messages_recycler;
    private ImageView open_cam_btn;
    private BoomMenuButton open_attachments;
    private StorageReference mStorage;
    private String chatUId = "";
    private String mCurrentUserId;
    private String chatImageUri = "";
    private String chatPhone = "";
    private FirebaseAuth firebaseAuth;
    private boolean recording = false;
    private Uri cameraPhotoUri;
    static float sAnimatorScale = 1;
    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();

    String mCurrentPhotoPath;
    private Conversation conversation;

    static final int REQUEST_TAKE_PHOTO = 1;

    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupWindowAnimations();
        db.setFirestoreSettings(settings);

        text_msg = (TextInputEditText) findViewById(R.id.text_msg);
        name_chat = (TextView) findViewById(R.id.name_chat);
        send_message_btn = (ImageView) findViewById(R.id.send_message_btn);
        send_audio_btn = (ImageView) findViewById(R.id.send_audio_btn);
        stop_audio_btn = (ImageView) findViewById(R.id.stop_audio_btn);
        lastSeen = (TextView) findViewById(R.id.lastSeen);

        open_cam_btn = (ImageView) findViewById(R.id.open_cam_btn);
        user_img = (CircleImageView) findViewById(R.id.user_img);
        open_attachments = (BoomMenuButton) findViewById(R.id.attach_file_btn);

        mStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUserId = firebaseAuth.getInstance().getUid();

        messageRefInChat = db.collection(DatabaseNode.MESSAGES);
        usersRef = db.collection(DatabaseNode.USERS);
        chatReference = db.collection(DatabaseNode.CHATS).document(mCurrentUserId);

        Bundle bundle = getIntent().getExtras();
        conversation = bundle.getParcelable("conversation");

        /*if (conversation != null) {
            name_chat.setText(conversation.getWith());
            MutualDateFormat timeAgo = new MutualDateFormat();
            //final String last_seen = timeAgo.getTimeAgo(Long.valueOf(b.getString("last_seen")), getApplicationContext());

            //lastSeen.setText(b.getBoolean("isOnline")?"online":last_seen);
            chatUId = conversation.getUid();

            OtherChatReference = db.collection(DatabaseNode.CHATS).document(chatUId);

            usersRef.document(chatUId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //MutualDateFormat mTimeAgo = new MutualDateFormat();
                    long lastTime = Long.parseLong(documentSnapshot.get("last_seen").toString());
                    String lastSeenTime = MutualDateFormat.getTimeAgo(lastTime, getApplicationContext());
                    lastSeen.setText(documentSnapshot.getBoolean("online") ?"online":lastSeenTime);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    lastSeen.setText("");
                }
            });
            //lastSeen.setText(?"online":last_seen);

            usersRef.document(chatUId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(e != null){
                        lastSeen.setText("");
                    }
                    if(!documentSnapshot.getBoolean("online")){
                        String lastSeenTime = MutualDateFormat.getTimeAgo(Long.parseLong(documentSnapshot.get("last_seen").toString()), getApplicationContext());
                        lastSeen.setText(lastSeenTime);
                    }else{
                        lastSeen.setText("online");
                    }

                }
            });

            chatPhone = conversation.getWithPhone();


            if(!conversation.getImg_uri().trim().isEmpty()){
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.avatar_contact)
                        .error(R.drawable.bg_outline_gray)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                chatImageUri = conversation.getImg_uri();
                Glide.with(ChatActivity.this).setDefaultRequestOptions(requestOptions).load(chatImageUri).thumbnail(0.5f).into(user_img);
            }else{
                user_img.setImageResource(R.drawable.avatar_contact);
            }
        }*/

        if (bundle != null) {
            Bundle b = bundle.getBundle("chat_details");
            name_chat.setText(b.getString("chat_name"));
            MutualDateFormat timeAgo = new MutualDateFormat();
            //final String last_seen = timeAgo.getTimeAgo(Long.valueOf(b.getString("last_seen")), getApplicationContext());

            //lastSeen.setText(b.getBoolean("isOnline")?"online":last_seen);
            chatUId = b.getString("uid");

            OtherChatReference = db.collection(DatabaseNode.CHATS).document(chatUId);

            usersRef.document(chatUId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //MutualDateFormat mTimeAgo = new MutualDateFormat();
                    long lastTime = Long.parseLong(documentSnapshot.get("last_seen").toString());
                    String lastSeenTime = MutualDateFormat.getTimeAgo(lastTime, getApplicationContext());
                    lastSeen.setText(documentSnapshot.getBoolean("online") ?"online":lastSeenTime);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    lastSeen.setText("");
                }
            });
            //lastSeen.setText(?"online":last_seen);

            usersRef.document(chatUId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(e != null){
                        lastSeen.setText("");
                    }
                    if(!documentSnapshot.getBoolean("online")){
                        String lastSeenTime = MutualDateFormat.getTimeAgo(Long.parseLong(documentSnapshot.get("last_seen").toString()), getApplicationContext());
                        lastSeen.setText(lastSeenTime);
                    }else{
                        lastSeen.setText("online");
                    }

                }
            });

            chatPhone = b.getString("chat_phone");


            if(!b.getString("image_uri").trim().isEmpty()){
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.avatar_contact)
                        .error(R.drawable.bg_outline_gray)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                chatImageUri = b.getString("image_uri");
                Glide.with(ChatActivity.this).setDefaultRequestOptions(requestOptions).load(b.getString("image_uri")).thumbnail(0.5f).into(user_img);
            }else{
                user_img.setImageResource(R.drawable.avatar_contact);
            }
        }


        chatReference.collection("conversations").document(chatUId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists()){
                    final Conversation shared = new Conversation(false, new Date().getTime(), chatImageUri);
                    //mine.setLastMsg("chat set at me");
                    //mine.setTimestamp(new Date().getTime());

                    usersRef.document(chatUId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            MutualUser user = documentSnapshot.toObject(MutualUser.class);
                            final Conversation mine = new Conversation(shared.isSeen(), shared.getStart_date(), chatImageUri);
                            mine.setWith(user.getName());
                            mine.setUid(user.getUId());
                            mine.setLastMsgType(1);
                            mine.setTimestamp(new Date().getTime());
                            mine.setWithPhone(user.getPhone());
                            chatReference.collection("conversations").document(chatUId).set(mine);
                        }
                    });

                    usersRef.document(mCurrentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            MutualUser user = documentSnapshot.toObject(MutualUser.class);
                            final Conversation others = new Conversation(shared.isSeen(), shared.getStart_date(), documentSnapshot.getString("image_uri"));
                            //others.setLastMsg("chat set at other");
                            //others.setTimestamp(mine.getTimestamp());
                            others.setWith(user.getName());
                            others.setUid(user.getUId());
                            others.setLastMsgType(1);
                            others.setTimestamp(new Date().getTime());
                            others.setWithPhone(user.getPhone());
                            OtherChatReference.collection("conversations").document(mCurrentUserId).set(others);
                        }
                    });



                }
            }
        });

        //mToolbar.setLogo(R.drawable.avatar_contact);
        //mToolbar.setTitle("some name");
        loadMessages();

        text_msg.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(!s.toString().trim().isEmpty()){
                    //switchToText(true);
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
                    //switchToText(false);
                    send_audio_btn.setVisibility(View.VISIBLE);
                    send_message_btn.setVisibility(View.GONE);
                }
            }
        });

        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //@TODO: button send click actions
                sendMessage(MessageType.TEXT, text_msg.getText().toString(), null);
                text_msg.setText("");
                send_audio_btn.setVisibility(View.VISIBLE);
                send_message_btn.setVisibility(View.GONE);
            }
        });

        send_audio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setMessage("Start recording?");
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAudioPlayer = new Audio();
                        mAudioPlayer.startRecording();
                        stop_audio_btn.setVisibility(View.VISIBLE);
                        recording = true;
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        stop_audio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setMessage("Stop recording?");
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        mAudioPlayer.stopRecording();
                        stop_audio_btn.setVisibility(View.INVISIBLE);
                        recording = false;
                        final StorageReference filepath = mStorage.child(DatabaseNode.RECORDINGS).child(mAudioPlayer.getFileUri().getLastPathSegment());
                        filepath.putFile(mAudioPlayer.getFileUri()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            sendMessage(MessageType.AUDIO, uri.toString(), mAudioPlayer.getFileUri().toString());
                                            dialogInterface.cancel();
                                        }
                                    });

                                }else{
                                    Toast.makeText(ChatActivity.this,"upload failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        final int resourceImages[] = {
                R.drawable.attach_gallery,
                R.drawable.attach_audio,
                R.drawable.attach_camera,
                R.drawable.attach_contact,
                R.drawable.attach_document,
                R.drawable.attach_video
        };



        for (int i = 0; i < open_attachments.getPiecePlaceEnum().pieceNumber(); i++) {
            SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
                    .normalImageRes(resourceImages[i])
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            switch(index){
                                case 0:
                                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    galleryIntent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(galleryIntent, "Choose Image"), MediaAction.PIC_FROM_GALLERY);
                                    break;
                                case 1:
                                    Intent audioIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    audioIntent.setType("audio/*");
                                    startActivityForResult(audioIntent,MediaAction.AUDIO);
                                    break;
                                case 2:
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File photo = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                                    cameraPhotoUri = Uri.fromFile(photo);
                                    startActivityForResult(cameraIntent,MediaAction.TAKE_PICTURE);
                                    //takePhoto();
                                    break;
                                case 3:
                                    Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                                    startActivityForResult(contactIntent,MediaAction.CONTACT);
                                    break;
                                case 4:
                                    String[] mimeTypes = {
                                            "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                            "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                                            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                            "text/plain",
                                            "application/pdf",
                                            "application/zip"
                                    };
                                    Intent documentIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    documentIntent.addCategory(Intent.CATEGORY_OPENABLE);

                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                                        documentIntent.setType(mimeTypes.length == 1 ?mimeTypes[0] : "*/*");
                                        if(mimeTypes.length > 0){
                                            documentIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                                        }
                                    }else {
                                        String mimeTypesStr = "";
                                        for(String mimeType : mimeTypes){
                                            mimeTypesStr += mimeType + "|";
                                        }
                                        documentIntent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                                    }

                                    startActivityForResult(Intent.createChooser(documentIntent, "ChooseFile"),MediaAction.DOCUMENT);
                                    break;
                                case 5:
                                    Intent videoIntent = new Intent(Intent.ACTION_PICK);
                                    videoIntent.setType("video/*");
                                    startActivityForResult(videoIntent,MediaAction.VIDEO);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
            open_attachments.addBuilder(builder);
        }
/*
        send_audio_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scaleout);
                        anim.setInterpolator(new OvershootInterpolator());
                        //send_audio_btn.startAnimation(anim);

                        mAudioPlayer = new Audio();
                        mAudioPlayer.startRecording();
                        break;
                    case MotionEvent.ACTION_UP:
                        mAudioPlayer.stopRecording();
                        //mAudioPlayer.fileExists();

                        final StorageReference filepath = mStorage.child(DatabaseNode.RECORDINGS).child(mAudioPlayer.getFileUri().getLastPathSegment());
                        filepath.putFile(mAudioPlayer.getFileUri()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            sendMessage(MessageType.AUDIO, uri.toString());
                                        }
                                    });

                                }else{
                                    Toast.makeText(ChatActivity.this,"upload failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //@TODO: send audio message
                        //sendMessage(MessageType.AUDIO,  mAudioPlayer.MediaLocation());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        /*if(MotionEvent.obtain(200,2000,MotionEvent.ACTION_UP,0,30)){

                        }*

                        //@TODO: Listen for moving finger on screen to terminate recording ...
                        break;
                    default:
                        break;
                }
                return false;
            }
        });*/

        open_cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //takePhoto();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, MediaAction.PIC_FROM_GALLERY);
            }
        });

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact_profile_intent = new Intent(ChatActivity.this, ContactProfileActivity.class);
                contact_profile_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(contact_profile_intent);
                overridePendingTransition(R.anim.fab3_show, R.anim.alpha);
            }
        });

    }

    private String getPath(Uri uri) {
        String scheme = uri.getScheme();

        if ("file".equalsIgnoreCase(scheme)) return uri.getPath();
        else if ("content".equalsIgnoreCase(scheme)) return getFilePathFromMedia(uri);

        return "";
    }

    private String getFilePathFromMedia(Uri uri) {
        String data = "_data";
        String[] projection = { data };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndex(data);
        if (cursor.moveToFirst()) {
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return "";
    }

    private void copyFile(final Uri uri) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    String path = getPath(uri);
                    File sourceFile = new File(path);
                    File destinationFile = new File(new File(android.os.Environment.getExternalStorageDirectory(), MediaPath.IMAGE_SENT_FOLDER) + "/" + uri.getLastPathSegment());
                    copyFile(sourceFile, destinationFile);
                }
            }
        }, 100);
    }

    private static boolean copyFile(File sourceFile, File destinationFile) {
        FileInputStream sourceStream = null;
        FileOutputStream destinationStream = null;
        try {
            sourceStream = new FileInputStream(sourceFile);
            destinationStream = new FileOutputStream(destinationFile);

            FileChannel destinationFileChannel = destinationStream.getChannel();
            FileChannel sourceFileChannel = sourceStream.getChannel();

            long fileSize = sourceFileChannel.size();
            int startPosition = 0;

            destinationFileChannel.transferFrom(sourceFileChannel, startPosition, fileSize);

            return true;
        }
        catch (IOException e) {
            // TODO log "I/O Error: " + e.getMessage()
            return false;
        }
        finally {
            // formatter:off
            try { sourceStream.close(); } catch (Exception e) { /* TODO log "Error closing source stream." */ }
            try { destinationStream.close(); } catch (Exception e) { /* TODO log "Error closing destination stream." */ }
            // formatter:on
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case MediaAction.PIC_FROM_GALLERY:
                    final Media img_from_gallery = new Media();
                    img_from_gallery.setFileUri(data.getData());
                    img_from_gallery.setStorageNode(DatabaseNode.IMAGES);
                    new MediaUpload(img_from_gallery){
                        @Override
                        public void onUploadSuccess(Media media) {
                            //copyFile(fileResult.getFileUri());
                            try{
                                sendMessage(MessageType.IMAGE, media.getDownloadUrl(), media.getFileUri().getPath());
                            }catch (Exception exc){
                                Log.d("mutual_image", exc.getMessage());
                            }

                        }

                        @Override
                        public void onPreUpload(String localFileLocation) {
                            super.onPreUpload(localFileLocation);
                        }
                    }.upload();
                    break;
                case MediaAction.AUDIO:
                    final Media audio_selected = new Media();
                    audio_selected.setFileUri(data.getData());
                    audio_selected.setStorageNode(DatabaseNode.AUDIO);
                    new MediaUpload(audio_selected){
                        @Override
                        public void onUploadSuccess(Media media) {
                            //copyFile(fileResult.getFileUri());
                            sendMessage(MessageType.AUDIO, media.getDownloadUrl(),  media.getFileUri().getPath());
                        }

                        @Override
                        public void onPreUpload(String localFileLocation) {
                            super.onPreUpload(localFileLocation);
                        }
                    }.upload();
                    break;
                case MediaAction.TAKE_PICTURE:
                    final Media img_from_camera = new Media();
                    img_from_camera.setFileUri(data.getData());
                    img_from_camera.setStorageNode(DatabaseNode.IMAGES);
                    new MediaUpload(img_from_camera){
                        @Override
                        public void onUploadSuccess(Media media)  {
                            //copyFile(fileResult.getFileUri());
                            sendMessage(MessageType.IMAGE, media.getDownloadUrl(), media.getFileUri().getPath());
                        }

                        @Override
                        public void onPreUpload(String localFileLocation) {
                            super.onPreUpload(localFileLocation);
                        }
                    }.upload();
                    break;
                case MediaAction.CONTACT:
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData,null,null,null,null);
                    if(c.moveToFirst()){
                        String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String name = c.getString(c.getColumnIndex(FROM_COLUMNS[0]));
                        //c.close();
                        //sendTextMessage(MessageType.TEXT, name);
                        //Log.d("tester",name + " : " + phone);
                        sendMessage(MessageType.CONTACT, name, phone);
                    }

                    break;
                case MediaAction.DOCUMENT:
                    final Media doc_from_chooser = new Media();
                    doc_from_chooser.setFileUri(data.getData());
                    doc_from_chooser.setStorageNode(DatabaseNode.DOCUMENTS);
                    new MediaUpload(doc_from_chooser){
                        @Override
                        public void onUploadSuccess(Media media)  {
                            sendMessage(MessageType.DOCUMENT, media.getDownloadUrl(), media.getFileUri().getLastPathSegment());
                        }

                        @Override
                        public void onPreUpload(String localFileLocation) {
                            super.onPreUpload(localFileLocation);
                        }
                    }.upload();
                    break;

            }
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
            anim.setDuration(200);
            anim.reset();
            send_audio_btn.clearAnimation();
            send_audio_btn.startAnimation(anim);
        }else{
            anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
            anim.setDuration(200);
            anim.reset();
            send_message_btn.clearAnimation();
            send_message_btn.startAnimation(anim);
        }
    }

    private void loadMessages(){

        Query query = messageRefInChat.document(mCurrentUserId).collection(chatUId).orderBy("time_sent", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>().setQuery(query,ChatMessage.class).build();

        chatItemsViewAdapter = new ChatMessagesViewAdapter(options,ChatActivity.this);
        chat_messages_recycler = (RecyclerView) findViewById(R.id.chat_messages);
        chat_messages_recycler.setHasFixedSize(true);
        chat_messages_recycler.setLayoutManager(new LinearLayoutManager(this));
        chat_messages_recycler.setAdapter(chatItemsViewAdapter);

        chatItemsViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chat_messages_recycler.smoothScrollToPosition(chatItemsViewAdapter.getItemCount());
            }
        });
    }



    private void sendMessage(int messageType, String message, String uriLocal){
        ChatMessage chatMessage;
        switch(messageType){
            case MessageType.IMAGE:
                chatMessage = new ImageMessage(FirebaseAuth.getInstance().getUid(), chatUId,message, uriLocal);
                break;
            case MessageType.AUDIO:
                chatMessage = new AudioMessage(FirebaseAuth.getInstance().getUid(), chatUId, message, uriLocal);
                break;
            case MessageType.TEXT:
                if(message.trim().isEmpty()){
                    return;
                }
                chatMessage = new TextMessage(FirebaseAuth.getInstance().getUid(),chatUId,message);
                break;
            case MessageType.DOCUMENT:
                chatMessage = new DocumentMessage(FirebaseAuth.getInstance().getUid(), chatUId, message, uriLocal);
                break;
            case MessageType.CONTACT:
                chatMessage = new ContactMessage(FirebaseAuth.getInstance().getUid(), chatUId, message, uriLocal);
                break;
            default:
                chatMessage = null;
                break;
        }

        CollectionReference messagesRef = FirebaseFirestore.getInstance().collection(DatabaseNode.MESSAGES).document(mCurrentUserId).collection(chatUId);
        CollectionReference messagesRefOther = FirebaseFirestore.getInstance().collection(DatabaseNode.MESSAGES).document(chatUId).collection(mCurrentUserId);
        if (chatMessage != null) {

            final Map<String, Object> convers = new HashMap<>();
            convers.put("lastMsg",chatMessage.getMessage());
            convers.put("timestamp", chatMessage.getTime_sent());
            convers.put("lastMsgStatus", chatMessage.getMessageStatus());
            convers.put("lastMsgType", chatMessage.getMessageType());
            final Notification notification = new Notification(mCurrentUserId,chatMessage.getMessage());

            messagesRef.add(chatMessage);
            messagesRefOther.add(chatMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    FirebaseFirestore.getInstance().collection(DatabaseNode.USERS).document(chatUId).collection("notifications").add(notification);
                }
            });

            chatReference.collection("conversations").document(chatUId).update(convers);
            OtherChatReference.collection("conversations").document(mCurrentUserId).update(convers);

            /*OtherChatReference.collection("conversations").document(mCurrentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    convers.put("count", Integer.valueOf(documentSnapshot.get("count").toString()) + 1);
                }
            });*/


            //chatReference.collection("conversations").document(chatUId).update("lastMsg",chatMessage.getMessage());
            //OtherChatReference.collection("conversations").document(mCurrentUserId).update("lastMsg",chatMessage.getMessage());
            //chatItemsViewAdapter.notifyDataSetChanged();

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

                    startActivityForResult(cameraIntent, MediaAction.TAKE_PICTURE);
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

        File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), MediaPath.IMAGE_SENT_FOLDER);

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
                break;
            case R.id.attach_file_btn:

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
