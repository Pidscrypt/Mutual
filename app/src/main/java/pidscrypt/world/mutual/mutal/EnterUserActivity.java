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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class EnterUserActivity extends AppCompatActivity {

    private Button btn_submit_user_details;
    private ImageView userImage;
    private EditText username;
    private FirebaseFirestore online_db;
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

        btn_submit_user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int wait = 0;

                //if(enterUser()){
                    Intent intent = new Intent(EnterUserActivity.this, LandingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                /*}else{
                    Toast.makeText(getBaseContext(), "failed to add data", Toast.LENGTH_SHORT).show();
                }*/

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

    private void enterUser(){

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        online_db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
