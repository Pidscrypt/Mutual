package pidscrypt.world.mutual.mutal;

import android.annotation.SuppressLint;
import android.app.Application;

import com.bumptech.glide.GlideBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MutualMessenger extends Application {

    private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        /* Glide */
        /*GlideBuilder glideBuilder = new GlideBuilder();
        glideBuilder.*/
    }
}
