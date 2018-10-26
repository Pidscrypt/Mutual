package pidscrypt.world.mutual.mutal.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

public class MutualPermission {

    private Activity mContext;
    private int requestCode;

    public MutualPermission() {
    }

    public MutualPermission(Context context, int requestCode){
        this.mContext = (Activity) context;
        this.requestCode = requestCode;
    }

    public void request(String permission){
        ActivityCompat.requestPermissions(mContext, new String[] { permission }, requestCode);
    }

    public boolean check(String[] permission){
        if(ActivityCompat.checkSelfPermission(mContext, permission[0]) != PackageManager.PERMISSION_GRANTED){
            new AlertDialog.Builder(mContext)
                    .setTitle("Permission request")
                    .setMessage(permission[1])
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            request(Manifest.permission.READ_PHONE_STATE);
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        }
        return false;
    }

    public void onPermissionGranted(){

    }

    public void onPermissionDenied(){

    }
}
