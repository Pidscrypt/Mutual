package pidscrypt.world.mutual.mutal.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import pidscrypt.world.mutual.mutal.Database.MutualDB;
import pidscrypt.world.mutual.mutal.LandingActivity;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Contact;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.services.Contacts;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class AppSetupActivity extends AppCompatActivity {

    private TextView tv_current_action;

    private Handler handler;
    private Runnable runnable;
    private Contacts contacts;

    private static final int SPLASH_TIME = 2000;

    private MutualDB mutualDB;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private MutualUser mutualUser;

    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setup);

        handler = new Handler();
        contacts = new Contacts(AppSetupActivity.this);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tv_current_action = (TextView) findViewById(R.id.tv_current_action);

        storeUser();
        setupMyContacts();
        goToLanding();
    }

    public void moveToItem(String action){
        tv_current_action.setText(action);
    }

    public void goToLanding(){
        moveToItem("Finalising setup");

        LandingActivity.startActivity(AppSetupActivity.this,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.fui_slide_out_left);
        finish();
    }

    public void storeUser(){
        moveToItem("saving user locally");
        mutualDB = new MutualDB(this);
        firestore.collection(DatabaseNode.USERS).document(firebaseUser.getPhoneNumber()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                mutualDB.storeUser(documentSnapshot.getString("name"),
                        documentSnapshot.getString("phone"),
                        documentSnapshot.getId(),
                        FirebaseInstanceId.getInstance().getToken()
                );
            }
        });

    }

    public void setupMyContacts(){

        final CollectionReference myContacts = firestore.collection(DatabaseNode.USERS).document(firebaseUser.getPhoneNumber()).collection(DatabaseNode.CONTACTS);

        runnable = new Runnable() {
            @Override
            public void run() {
                List<String> eraser = new ArrayList<>();
                moveToItem("Gathering Contact data ");

                // cursor get all contacts in the phone
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null,FROM_COLUMNS[0] + " ASC");

                if(cursor.moveToFirst()){

                    while(cursor.moveToNext()){
                        String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if(phone.startsWith("0")){
                            //@TODO: change this to auto detect country code
                            phone = "+256" + phone.substring(1);
                        }
                        if(phone.contains(" ")){
                            phone = phone.replaceAll(" ","");
                        }
                        if(eraser.contains(phone)){
                            continue;
                        }

                        final Contact cont = new Contact(cursor.getString(cursor.getColumnIndex(FROM_COLUMNS[0])), phone);

                        // check if user exists. if so, store the contact under my contacts
                        firestore.collection(DatabaseNode.USERS).document(cont.getNumber()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                cont.setUserId(documentSnapshot.getString("uid"));
                                Log.d("CONTACT", cont.toString());
                                myContacts.document(cont.getNumber()).set(cont);
                            }
                        });

                        eraser.add(phone);
                    }
                }

                cursor.close();

                handler.sendEmptyMessage(0);

            }
        };
        handler.postDelayed(runnable, SPLASH_TIME);
    }


    public void setupMyMutualsContacts(){

        final CollectionReference myContacts = firestore.collection(DatabaseNode.USERS).document(firebaseUser.getUid()).collection(DatabaseNode.MUTUALS);

        runnable = new Runnable() {
            @Override
            public void run() {
                List<String> eraser = new ArrayList<>();

                moveToItem("Setup mutuals");

                firestore.collection(DatabaseNode.USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot doc : task.getResult().getDocuments()){

                            //@todo: not making sense yet
                            firestore
                                    .collection(DatabaseNode.USERS)
                                    .document(doc.getId())
                                    .collection(DatabaseNode.CONTACTS)
                                    .document(firebaseUser.getPhoneNumber())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            //Contact cont = new Contact();
                                            //firestore.collection(DatabaseNode.USERS).document(firebaseUser.getPhoneNumber()).collection(DatabaseNode.MUTUALS).add(cont);
                                        }
                                    });
                        }
                    }
                });

                // cursor get all contacts in the phone
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null,FROM_COLUMNS[0] + " ASC");

                if(cursor.moveToFirst()){

                    while(cursor.moveToNext()){
                        String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if(phone.startsWith("0")){
                            //@TODO: change this to auto detect country code
                            phone = "+256" + phone.substring(1);
                        }
                        if(phone.contains(" ")){
                            phone = phone.replaceAll(" ","");
                        }
                        if(eraser.contains(phone)){
                            continue;
                        }

                        final Contact cont = new Contact(cursor.getString(cursor.getColumnIndex(FROM_COLUMNS[0])), phone);

                        // check if user exists. if so, store the contact under my contacts
                        firestore.collection(DatabaseNode.USERS).document(cont.getNumber()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                cont.setUserId(documentSnapshot.getString("uid"));
                                Log.d("CONTACT", cont.toString());
                                myContacts.document(cont.getNumber()).set(cont);
                            }
                        });

                        eraser.add(phone);
                    }
                }

                cursor.close();

                LandingActivity.startActivity(AppSetupActivity.this,Intent.FLAG_ACTIVITY_CLEAR_TOP);

                handler.sendEmptyMessage(0);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.fui_slide_out_left);
                finish();

            }
        };
        handler.postDelayed(runnable, SPLASH_TIME);
    }
}
