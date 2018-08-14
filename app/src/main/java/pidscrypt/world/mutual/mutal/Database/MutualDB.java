package pidscrypt.world.mutual.mutal.Database;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;

import pidscrypt.world.mutual.mutal.errors.Error;
import pidscrypt.world.mutual.mutal.errors.MutualError;
import pidscrypt.world.mutual.mutal.services.DataTables;

public class MutualDB implements Database {
    SQLiteDatabase db = null;
    Thread uiThread = null;
    Runnable runnable = null;
    private String dbName = null;

    public void Open(){
        db = SQLiteDatabase.openOrCreateDatabase(this.getDatabaseName(),null,null);
    }
    public SQLiteDatabase getInstance(){
        return this.db;
    }

    public void closeInstance(){
        if(this.getInstance() != null){
            this.getInstance().close();
        }
    }

    public void CreateTables(){
     runnable = new Runnable() {
         @SuppressLint("StaticFieldLeak")
         @Override
         public void run() {
             MutualDB mutualDB = new MutualDB();
             mutualDB.setDatabaseName("mutual");
             mutualDB.getInstance();
             mutualDB.Open();

             onStartCreate();

            synchronized (this){
                new DataTables(){
                    @Override
                    public void completionStatus(Boolean response) {
                        super.completionStatus(response);
                        if(response){
                            onCreateComplete();
                        }
                    }
                }.execute(mutualDB);
            }

            mutualDB.closeInstance();
            onCreateComplete();
         }
     };

     uiThread =  new Thread(null,this.runnable);
    }

    public void setDatabaseName(String dbname){
        this.dbName = dbname;
    }

    public String getDatabaseName(){
        return this.dbName;
    }

    @Override
    public boolean onCreateComplete() {
        return false;
    }

    @Override
    public boolean onStartCreate() {
        return false;
    }

    @Override
    public void onError() {
        MutualError err = new MutualError();
        err.setMessage("Problem connecting to DB");
        err.show();
    }
}
