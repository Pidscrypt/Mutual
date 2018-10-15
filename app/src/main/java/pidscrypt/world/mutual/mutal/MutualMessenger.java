package pidscrypt.world.mutual.mutal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.GlideBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.UserStatus;

public class MutualMessenger extends MultiDexApplication {

    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference myDoc;

    @Override
    public void onTerminate() {
        super.onTerminate();
        if(mAuth != null){
            myDoc.update("online", UserStatus.OFFLINE);
            myDoc.update("last_seen", new Date().getTime());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();

        /* Glide */
        /*GlideBuilder glideBuilder = new GlideBuilder();
        glideBuilder.*/

        if(mAuth.getCurrentUser() != null) {

            db = FirebaseFirestore.getInstance();

            myDoc = db.collection(DatabaseNode.USERS).document(mAuth.getCurrentUser().getUid());

            myDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        myDoc.update("online", UserStatus.ONLINE);
                    }
                }
            });

            /*myDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(documentS){
                        Map<String, Object> online_status = new HashMap<>();
                        myDoc.update(online_status);
                    }
                }
            });*/

        }
    }
}
