package pidscrypt.world.mutual.mutal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.sql.Date;

import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.user.MutualUser;

import static java.lang.Thread.sleep;

public class EnterUserActivity extends AppCompatActivity {

    private Button btn_submit_user_details;
    private ImageView userImage;
    private EditText username, user_tag;
    private FirebaseAuth userAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        //checkUserExists();

        btn_submit_user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int wait = 0;
                /*Intent intent = new Intent(EnterUserActivity.this, LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
                addUser();
                //enterUser(username.getText().toString());

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
    protected void onStart() {
        super.onStart();

    }

    private void addUser(){
        //String user = userAuth.getUid();
        FirebaseUser user = userAuth.getCurrentUser();
        if(user != null){
            DocumentReference userAddRef = db.collection(DatabaseNode.USERS).document(user.getUid());
            Intent i = getIntent();
            String mobile = i.getStringExtra("phone");
            userAddRef.set(new MutualUser(username.getText().toString(),mobile,"","focus")).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(EnterUserActivity.this,LandingActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }else{
                        Toast.makeText(EnterUserActivity.this, "could not add user",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EnterUserActivity.this,"failed to complete request",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void updateUser(){
        final String user = userAuth.getUid();
        CollectionReference userUpdateRef = db.collection(DatabaseNode.USERS);
        userUpdateRef.add(new MutualUser("test user","+256772651234",null,"focus"));

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
