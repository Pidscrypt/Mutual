package pidscrypt.world.mutual.mutal;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentListenOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;

public class MyProfileActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private FirebaseUser user;
    private DocumentReference myDocRef;
    private CircleImageView myImage;
    private TextView display_name, display_status;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    ///private FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mFirestore.setFirestoreSettings(settings);

        display_name = (TextView) findViewById(R.id.display_name);
        display_status = (TextView) findViewById(R.id.display_status);
        myImage = (CircleImageView) findViewById(R.id.my_image);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        myDocRef = mFirestore.collection(DatabaseNode.USERS).document(user.getUid());

        mProgress = new ProgressDialog(MyProfileActivity.this);
        mProgress.setTitle("Fetching data!");
        mProgress.setMessage("please wait ...");
        mProgress.show();

        myDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String Thumb_uri = documentSnapshot.getString("image_uri");
                String name = documentSnapshot.getString("name");
                String status = documentSnapshot.getString("status");

                display_name.setText(name);
                display_status.setText(status);

                if(Thumb_uri != null && !Thumb_uri.trim().isEmpty()){
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.drawable.avatar_contact)
                            .error(R.drawable.bg_outline_gray)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);

                    Glide.with(MyProfileActivity.this).setDefaultRequestOptions(requestOptions).load(Thumb_uri).thumbnail(0.5f).into(myImage);
                }

                mProgress.dismiss();
            }
        });

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    startActivityForResult(Intent.createChooser(galleryIntent, "CHOOSE PROFILE IMAGE"),1);
                }else{
                    startActivityForResult(galleryIntent,1);
                }

                // start picker to get image for cropping and then use the image in cropping activity
                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MyProfileActivity.this);*/
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri image_uri = data.getData();
            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(image_uri)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mProgress = new ProgressDialog(MyProfileActivity.this);
                mProgress.setTitle("Updating Image");
                mProgress.setMessage("please wait while we update status!");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                final StorageReference filepath = mStorage.child("profile_images").child(resultUri.getLastPathSegment());
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final String download_url = uri.toString();

                                    myDocRef.update("image_uri",download_url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Glide.with(MyProfileActivity.this).load(download_url).into(myImage);
                                            mProgress.dismiss();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgress.dismiss();
                                    Toast.makeText(MyProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mProgress.dismiss();
                        }else{
                            mProgress.dismiss();
                            Toast.makeText(MyProfileActivity.this,"image upload failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
