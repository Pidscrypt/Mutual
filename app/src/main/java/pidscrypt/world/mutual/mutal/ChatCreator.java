package pidscrypt.world.mutual.mutal;

import android.os.AsyncTask;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import pidscrypt.world.mutual.mutal.api.Chat;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class ChatCreator extends AsyncTask<List<MutualUser>, Void, Boolean> {
    DocumentReference doc = null;
    @Override
    protected Boolean doInBackground(List<MutualUser>... users) {

        /*Chat newChat = new Chat(users[0], null);
        CollectionReference myFirebaseDoc = FirebaseFirestore.getInstance().collection(DatabaseNode.CHATS);
        myFirebaseDoc.add(newChat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                doc = documentReference;
            }
        });*/

        return doc != null;
    }

    @Override
    protected void onPostExecute(Boolean res) {
        super.onPostExecute(res);
        isProcessDone(res, doc);
    }

    public void isProcessDone(Boolean result, DocumentReference documentReference){

    }
}
