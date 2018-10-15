package pidscrypt.world.mutual.mutal.media;

import android.content.Intent;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class Media {
    private Uri fileUri;
    private String localFileLocation;
    private String storageNode;
    private String downloadUrl;
    private String firebaseStorageLocation;

    public Media(){
    }

    public String getFirebaseStorageLocation() {
        return firebaseStorageLocation;
    }

    public void setFirebaseStorageLocation(String firebaseStorageLocation) {
        this.firebaseStorageLocation = firebaseStorageLocation;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getLocalFileLocation() {
        return localFileLocation;
    }

    public String getStorageNode() {
        return storageNode;
    }

    public void setFileUri(Uri fileUri) {
        setLocalFileLocation(fileUri.toString());
        this.fileUri = fileUri;
    }

    public void setLocalFileLocation(String localFileLocation) {
        this.localFileLocation = localFileLocation;
    }

    public void setStorageNode(String storageNode) {
        this.storageNode = storageNode;
    }

}
