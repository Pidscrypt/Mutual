package pidscrypt.world.mutual.mutal.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pidscrypt.world.mutual.mutal.R;

public class MutualFirebaseMessagingService extends FirebaseMessagingService {

    private final static String TAG = "MessagingService";
    public MutualFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
/*
        if(remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "Message data: "+remoteMessage.getData().toString());
        }

        if(remoteMessage.getNotification() != null){

            String notification_title = remoteMessage.getNotification().getTitle();
            String notification_message = remoteMessage.getNotification().getBody();

            String click_action = remoteMessage.getNotification().getClickAction();

            String from_user_id = remoteMessage.getData().get("from_user_id");

            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent resultIntent = new Intent(click_action);
            resultIntent.putExtra("user_id", from_user_id);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(notification_title)
                            .setContentText(notification_message)
                            .setSound(notificationSound)
                            .setContentIntent(resultPendingIntent);


            int mNotificationId = (int) System.currentTimeMillis();

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
*/
    }
}
