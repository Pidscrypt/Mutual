package pidscrypt.world.mutual.mutal.user;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pidscrypt.world.mutual.mutal.Database.MutualDB;

public class MutualUser {

    private boolean mExists = false;
    private Cursor cursor;
    private MutualDB db;
    private String status;

    public MutualUser(){
        //db = this.db.getInstance();
    }

    public Cursor Users(){
        this.db.Open();
        this.cursor = this.db.getInstance().rawQuery("select * from user ",null);
        return cursor;
    }

    public String getUserStatus(){
        if(this.Users().getCount() > 0){
            cursor.moveToFirst();
            this.status = cursor.getString(cursor.getColumnIndex("status"));
        }else{
            return null;
        }
        return this.status;
    }

}
