package pidscrypt.world.mutual.mutal.FileTree;

import android.content.Context;

import java.io.File;

public class Setup {
    File appDir;

    public boolean createDir(Context base){

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            appDir = new File(android.os.Environment.getExternalStorageDirectory(), "Mutual/images/received");
        } else {
            appDir = base.getCacheDir();
        }

        if (!appDir.exists()){
            try {
                appDir.mkdirs();
                return true;
            }catch (Exception ex){
                return false;
            }
        }

        return false;
    }
}
