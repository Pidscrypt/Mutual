package pidscrypt.world.mutual.mutal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.text.format.DateFormat;

//import com.google.firebase.firestore.FirebaseFirestore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.fabric.sdk.android.Fabric;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.MessageType;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.api.UserStatus;
import pidscrypt.world.mutual.mutal.services.MutualFirebaseIdInstanceService;
import pidscrypt.world.mutual.mutal.user.MutualUser;

import static java.lang.Thread.sleep;

public class EnterUserActivity extends AppCompatActivity {

    private Button btn_submit_user_details;
    private CircleImageView userImage;
    private EditText username;
    private String uid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private CollectionReference usersRef = db.collection(DatabaseNode.USERS);
    private FirebaseAuth.AuthStateListener firebaseAuthState;
    private ProgressDialog mProgress;
    private String image_uri = null;
    private ProgressBar upload_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        btn_submit_user_details = (Button) findViewById(R.id.btn_submit_user_details);
        userImage = (CircleImageView) findViewById(R.id.user_img);
        username = (EditText) findViewById(R.id.user_name);
        upload_progress = (ProgressBar) findViewById(R.id.upload_progress);

        uid = FirebaseAuth.getInstance().getUid();
        final String phone = getIntent().getStringExtra("phone");
        checkUserExists();

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    startActivityForResult(Intent.createChooser(galleryIntent, "CHOOSE PROFILE IMAGE"),1);
                }else{
                    startActivityForResult(galleryIntent,1);
                }
            }
        });

        btn_submit_user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uid != null){

                    btn_submit_user_details.setEnabled(false);

                    DocumentReference userRef = FirebaseFirestore.getInstance().collection(DatabaseNode.USERS).document(uid);
                    String user_name = username.getText().toString();
                    if(user_name.trim().isEmpty()){
                        username.setError("please enter user name");
                        username.requestFocus();
                    }else{
                        long last_seen = new Date().getTime();
                        MutualUser user = new MutualUser(
                                user_name,
                                phone,
                                image_uri != null?image_uri:"",
                                "Hi! Look, am using Mutual Chat!",
                                uid,
                                last_seen,
                                UserStatus.ONLINE
                        );
                        user.setDevice_token(FirebaseInstanceId.getInstance().getToken());

                        userRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EnterUserActivity.this,"Success!", Toast.LENGTH_SHORT).show();
                                Intent landing = new Intent(EnterUserActivity.this, LandingActivity.class);
                                landing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(landing);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        /*userRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(EnterUserActivity.this,"Success!", Toast.LENGTH_SHORT).show();
                            }
                        });*/
                    }
                }

            }
        });

        firebaseAuthState = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                //FireBaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    //userImage;
                    //@TODO: load userImage and info into the views. if no account yet.
                }else{
                    Intent welcomeIntent = new Intent(EnterUserActivity.this,Welcome.class);
                    welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(welcomeIntent);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri image_uri = data.getData();
            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(image_uri)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                /*mProgress = new ProgressDialog(EnterUserActivity.this);
                mProgress.setTitle("Updating Image");
                mProgress.setMessage("please wait while we update status!");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();*/

                upload_progress.setVisibility(View.VISIBLE);

                Uri resultUri = result.getUri();

                File thumb_filePathRef = new File(resultUri.getPath());
                final byte[] thumb_byte;

                try {
                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePathRef);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    thumb_byte = byteArrayOutputStream.toByteArray();

                    final StorageReference filepath = mStorage.child(DatabaseNode.PROFIILE_IMAGES).child(resultUri.getLastPathSegment());
                    final StorageReference thumb_filepath = mStorage.child(DatabaseNode.PROFIILE_IMAGES).child(DatabaseNode.THUMBNAILS).child(resultUri.getLastPathSegment());

                    filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        image_uri = uri.toString();

                                        UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                                //@todo: save thumbnail details too.
                                            }
                                        });

                                        Glide.with(EnterUserActivity.this).load(image_uri).into(userImage);
                                        //mProgress.dismiss();
                                        upload_progress.setVisibility(View.GONE);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //mProgress.dismiss();
                                        upload_progress.setVisibility(View.GONE);
                                        Toast.makeText(EnterUserActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //mProgress.dismiss();
                                upload_progress.setVisibility(View.GONE);
                            }else{
                                //mProgress.dismiss();
                                upload_progress.setVisibility(View.GONE);
                                Toast.makeText(EnterUserActivity.this,"image upload failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateUser(){
        final String user;// = userAuth.getUid();
        CollectionReference userUpdateRef = db.collection(DatabaseNode.USERS);
        //userUpdateRef.add(new MutualUser("test user","+256772651234",null,"focus"));

    }

    private void checkUserExists(){
        final String user = FirebaseAuth.getInstance().getUid();
        if(user != null){
            final DocumentReference userRef = db.collection(DatabaseNode.USERS).document(user);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            final String user_name = document.getString("name");

                            username.setText(user_name);
                            if(!document.getString("image_uri").equals("")){

                                RequestOptions requestOptions = new RequestOptions()
                                        .placeholder(R.drawable.avatar_contact)
                                        .error(R.drawable.avatar_contact)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                                Glide.with(EnterUserActivity.this).setDefaultRequestOptions(requestOptions).load(document.getString("image_url")).thumbnail(0.5f).into(userImage);

                                //Glide.with(getApplicationContext()).load(document.getString("image_url")).into(userImage);
                            }else{
                                userImage.setImageResource(R.drawable.avatar_contact);
                            }

                        }
                    }else{
                        //@TODO conn error here
                        Toast.makeText(EnterUserActivity.this, "Could not connect to firebase",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
