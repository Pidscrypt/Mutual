package pidscrypt.world.mutual.mutal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.format.DateFormat;

//import com.google.firebase.firestore.FirebaseFirestore;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.MessageType;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.user.MutualUser;

import static java.lang.Thread.sleep;

public class EnterUserActivity extends AppCompatActivity {

    private Button btn_submit_user_details;
    private ImageView userImage;
    private Uri image_uri;
    private EditText username, user_tag;
    private String uid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private CollectionReference usersRef = db.collection(DatabaseNode.USERS);
    private FirebaseAuth.AuthStateListener firebaseAuthState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btn_submit_user_details = (Button) findViewById(R.id.btn_submit_user_details);
        userImage = (ImageView) findViewById(R.id.user_img);
        username = (EditText) findViewById(R.id.user_name);
        user_tag = (EditText) findViewById(R.id.user_tag);

        uid = FirebaseAuth.getInstance().getUid();
        final String phone = getIntent().getStringExtra("phone");
        checkUserExists();

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });

        btn_submit_user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uid != null){

                    DocumentReference userRef = FirebaseFirestore.getInstance().collection(DatabaseNode.USERS).document(uid);
                    String user_name = username.getText().toString();
                    if(user_name.trim().isEmpty()){
                        username.setError("please enter user name");
                        username.requestFocus();
                    }else{
                        Map<String, String> user = new HashMap<>();
                        user.put("name", user_name);
                        user.put("phone",phone);
                        user.put("status",user_tag.getText().toString().trim().isEmpty()?"Hi! Look, am using Mutual Chat!":user_tag.getText().toString().trim());
                        user.put("image_uri", image_uri != null?String.valueOf(image_uri):"");
                        user.put("image_thumbnail", image_uri != null?String.valueOf(image_uri):"");
                        user.put("uid",uid);
                        user.put("reg_date", String.valueOf(new Date().getTime()));

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK){
            //sendMessage(MessageType.IMAGE, data);
            Uri imageUri = data.getData();
            final StorageReference filepath = mStorage.child("images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image_uri = taskSnapshot.getUploadSessionUri();
                    Glide.with(EnterUserActivity.this).load(image_uri).into(userImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EnterUserActivity.this,"image upload failed",Toast.LENGTH_SHORT).show();
                }
            });
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
                                Glide.with(getApplicationContext()).load(document.getString("image_url")).into(userImage);
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
