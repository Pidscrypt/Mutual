package pidscrypt.world.mutual.mutal.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Contact;
import pidscrypt.world.mutual.mutal.api.DatabaseNode;
import pidscrypt.world.mutual.mutal.user.MutualUser;

public class Contacts {

    private Context mContext;
    private CollectionReference users = FirebaseFirestore.getInstance().collection(DatabaseNode.USERS);

    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    };

    public Contacts() {
    }

    public Contacts(Context mContext) {
        this.mContext = mContext;
    }

    public List<Contact> fetch(){

        List<Contact> list = new ArrayList<>();
        List<String> eraser = new ArrayList<>();

        // cursor get all contacts in the phone
        Cursor cursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null,FROM_COLUMNS[0] + " ASC");

        cursor.moveToFirst();

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
            final Contact cont = new Contact(
                    cursor.getString(cursor.getColumnIndex(FROM_COLUMNS[0])),
                    phone,
                    R.drawable.avatar_contact);

            //@TODO: check firebase for contact exists
            Query usersQuery = users.whereEqualTo("phone",phone);
            usersQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    String obj = queryDocumentSnapshots.getDocuments().get(0).get("image_uri").toString();
                    cont.setImage_uri(obj);
                }
            });
            list.add(cont);

            eraser.add(phone);
        }

        return list;
    }
    }