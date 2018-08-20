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
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.firestore.FirebaseFirestore;

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

import pidscrypt.world.mutual.mutal.user.User;

import static java.lang.Thread.sleep;

public class EnterUserActivity extends AppCompatActivity {

    private Button btn_submit_user_details;
    private ImageView userImage;
    private EditText username;
    private TextView test_text;
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
        test_text = (TextView) findViewById(R.id.test_text);

        checkUserExists();

        btn_submit_user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int wait = 0;
                Intent intent = new Intent(EnterUserActivity.this, LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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

    private void checkUserExists(){
        final String user = FirebaseAuth.getInstance().getUid();
        online_db = FirebaseFirestore.getInstance();
        if(user != null){
            final DocumentReference userRef = online_db.collection("users").document(user);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            final String user_name = document.getString("name");

                            username.setText(user_name);

                        }
                    }else{
                        //@TODO conn error here
                        Toast.makeText(EnterUserActivity.this, "Could not connect to firebase",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void enterUser(String uname) {

        User user = new User(uname,"focus","","");
// Add a new document with a generated ID

            online_db = FirebaseFirestore.getInstance();
            String uid = FirebaseAuth.getInstance().getUid();

        CollectionReference userRef = online_db.collection("users");

        userRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(EnterUserActivity.this, LandingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        /*if(uid != null){

            DocumentReference docuref = online_db.collection("users").document(uid);

        }*/
    }

}
