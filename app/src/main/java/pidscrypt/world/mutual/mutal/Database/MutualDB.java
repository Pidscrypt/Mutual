package pidscrypt.world.mutual.mutal.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MutualDB extends SQLiteOpenHelper {

    private static String DB_NAME = "mutual";
    private SQLiteDatabase sqLiteDatabase;

    private SecretKey mKey;
    private KeyGenerator kGen;
    private Cipher cipher;

    public MutualDB(Context context){
        super(context, DB_NAME, null, 1);

        mKey = null;
        try{
            kGen = KeyGenerator.getInstance("AES");
            mKey = kGen.generateKey();
            //Log.d("cipher",mKey.toString());
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,mKey);

            //Log.d("cipher",cipher.toString());

        }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table if not EXISTS devices(id INTEGER PRIMARY KEY AUTOINCREMENT, name varchar,number varchar, status varchar)");
        db.execSQL("create table if not EXISTS users(uid varchar PRIMARY KEY, name varchar,phone varchar, device_token varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDevice(String name, String number){
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name",name);
        values.put("number", number);
        values.put("status", "off");

        sqLiteDatabase.insert("devices",null,values);
        sqLiteDatabase.close();
    }

    public void updateDevice(String id, String name, String number, String status){
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name",name);
        values.put("number", number);
        values.put("status", status);

        sqLiteDatabase.update("devices",values, "id = ?", new String[]{ id });
        sqLiteDatabase.close();
    }

    public void getAllDevices(){
        sqLiteDatabase = this.getReadableDatabase();
        /*ArrayList<Device> devices = new ArrayList<>();

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM devices", null);
        if(c.moveToFirst()){
            do {
                String id = c.getString(0);
                String name = c.getString(1);
                String phone = c.getString(2);
                String status = c.getString(3);
                devices.add(new Device(id,name,phone,status.equals("on")?true:false));

                Log.e("DBNAME", name);
                Log.e("DBPHONE", phone);
                Log.e("DBSTATUS", status);
            }while(c.moveToNext());
        }*/
        sqLiteDatabase.close();
        //c.close();
    }

    public void getUser(String uid){

        sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE uid = '?' ", new String[]{ uid });
        c.close();
    }

    public void storeUser(String name, String number, String uid, String device_token) {

        sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users", null);

        if(c.moveToFirst()){

            sqLiteDatabase = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("name",name);
            values.put("phone", number);
            values.put("uid", uid);
            values.put("device_token", device_token);

            sqLiteDatabase.update("users",values, "uid = ?", new String[]{ uid });
            sqLiteDatabase.close();
        }else{

            sqLiteDatabase = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("name",name);
            values.put("phone", number);
            values.put("uid", uid);
            values.put("device_token", device_token);

            sqLiteDatabase.insert("users",null,values);
            sqLiteDatabase.close();

        }
        c.close();
    }
}

