package pidscrypt.world.mutual.mutal.media;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import pidscrypt.world.mutual.mutal.ChatActivity;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.api.MessageType;

public class MediaUpload {
    private StorageReference firebaseStorage;
    private Media media;
    private boolean uploaded = false;
    private Thread thread;

    public MediaUpload(Media media){
        this.firebaseStorage = FirebaseStorage.getInstance().getReference();
        this.media = media;
    }

    public void upload(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    final StorageReference filepath = firebaseStorage.child(media.getStorageNode()).child(media.getFileUri().getLastPathSegment());
                    filepath.putFile(media.getFileUri()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        media.setDownloadUrl(uri.toString());
                                        media.setFirebaseStorageLocation(uri.getPath());
                                        uploaded = true;
                                        onUploadSuccess(media);
                                    }
                                });
                                //filepath.getMetadata().getResult().getSizeBytes();
                            }else{
                                uploaded = false;
                            }
                        }
                    });
                }

            }
        };

        thread = new Thread(null, runnable);
        thread.start();
    }

    public void onUploadSuccess(Media media){

    }

    public void onPreUpload(String localFileLocation){

    }
}
