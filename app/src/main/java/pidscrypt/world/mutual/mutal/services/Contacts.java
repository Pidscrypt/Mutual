package pidscrypt.world.mutual.mutal.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.Contact;

public class Contacts {

    private Context mContext;

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

            //@TODO: check firebase for contact exists
            list.add(
                    new Contact(
                            cursor.getString(cursor.getColumnIndex(FROM_COLUMNS[0])),
                            phone,
                            R.drawable.avatar_contact));

            eraser.add(phone);
        }

        return list;
    }
    }