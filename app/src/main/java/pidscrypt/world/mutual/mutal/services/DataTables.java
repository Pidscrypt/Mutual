package pidscrypt.world.mutual.mutal.services;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import pidscrypt.world.mutual.mutal.Database.MutualDB;

public class DataTables extends AsyncTask<MutualDB,Void,Boolean> {

    @Override
    protected Boolean doInBackground(MutualDB... params) {
        MutualDB mutualDB = params[0];
//        try{
//            SQLiteDatabase db = mutualDB.getInstance();
//
//            db.execSQL("create table if not EXISTS user(_id INTEGER PRIMARY KEY AUTOINCREMENT, userID varchar, fname TEXT, lname TEXT,mobile TEXT,status varchar,img TEXT,key TEXT,status_statement TEXT,dob TEXT)");
//
//            db.execSQL("create table if not EXISTS gallery(_id INTEGER PRIMARY KEY AUTOINCREMENT, username varchar,url varchar, type varchar , folder TEXT)");
//
//            db.execSQL("create table if not EXISTS chat_room(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    " message_id varchar, message TEXT,created_at TEXT," +
//                    " from_img TEXT,from_userID TEXT,from_name varchar,from_gcm varchar,counter varchar,chatID TEXT)");
//            db.execSQL("create table if not EXISTS mutuals(_id INTEGER PRIMARY KEY AUTOINCREMENT,img TEXT,from_name varchar,from_gcm varchar,chatID varchar,status TEXT, mutuals_count varchar,mutual_friends TEXT,isFollowed varchar)");
//            db.execSQL("create table if not EXISTS chat(_id INTEGER PRIMARY KEY AUTOINCREMENT, message_id varchar, message TEXT,created_at TEXT,from_gcm varchar,userID varchar,chatID varchar, status varchar,messagetype varchar,messagesize varchar,filepath varchar,caption TEXT)");
//            db.execSQL("create table if not EXISTS contacts(_id INTEGER PRIMARY KEY AUTOINCREMENT, contact_id varchar, contact_no varchar,name varchar, status varchar, active varchar,isFollowed varchar)");
//            db.execSQL("create table if not EXISTS phonebook(_id INTEGER PRIMARY KEY AUTOINCREMENT, contact_no varchar,name varchar, gcm varchar, status varchar, isregistered varchar, userimage varchar,isFollowed varchar)");
//            db.execSQL("create table if not EXISTS feed(_id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id varchar,caption TEXT, image TEXT,username varchar,userimage varchar, likescount varchar,liked varchar,comments varchar,comments_count varchar,userID varchar,date varchar)");
//            db.execSQL("create table if not EXISTS feed_comments(_id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id varchar,comment_id varchar,comment TEXT, username varchar, userimage TEXT,userID varchar)");
//            db.execSQL("create table if not EXISTS folders(_id INTEGER PRIMARY KEY AUTOINCREMENT, foldername TEXT)");
//            db.execSQL("create table if not EXISTS groups(_id INTEGER PRIMARY KEY AUTOINCREMENT, groupname TEXT, channelid varchar, createdby varchar, createdon varchar, groupimg TEXT, total_members varchar,group_admin varchar)");
//            db.execSQL("create table if not EXISTS groupmembers(_id INTEGER PRIMARY KEY AUTOINCREMENT, name varchar, img TEXT, status varchar, tel varchar, channelid varchar, isadmin varchar)");
//            db.execSQL("create table if not EXISTS notifications(_id INTEGER PRIMARY KEY AUTOINCREMENT, notif_id varchar, notification_message varchar, userid varchar, img varchar, feed_id varchar, isread varchar, created_on varchar)");
//            db.execSQL("create table if not EXISTS settings(_id INTEGER PRIMARY KEY AUTOINCREMENT, account_status varchar, notification_lights varchar, notification_vib varchar, notification_sound varchar, img varchar, feed_id varchar, isread varchar, created_on varchar)");
//            db.execSQL("create table if not EXISTS broadcast(_id INTEGER PRIMARY KEY AUTOINCREMENT, broadcast TEXT, broadcastID varchar, createdby varchar, createdon varchar, groupimg TEXT, total_members varchar,group_admin varchar)");
//            db.execSQL("create table if not EXISTS feed_likes(_id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id varchar,comment_id varchar,comment TEXT, username varchar, userimage TEXT,userID varchar)");
//
//        }catch(Exception ex){
//
//        } finally{
//            // to be one later
//
//        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        completionStatus(result);
    }

    public void completionStatus(Boolean res) {

    }

}
