package pidscrypt.world.mutual.mutal.Database;

import android.database.sqlite.SQLiteDatabase;

public interface Database {
    boolean onCreateComplete();
    boolean onStartCreate();
    void onError();
}
