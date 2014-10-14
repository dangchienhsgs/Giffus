package com.dangchienhsgs.giffus;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dangchienhsgs.giffus.utils.Common;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG="GCMBroadcastReceiver";
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        PowerManager powerManager=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();
        try{
            GoogleCloudMessaging googleCloudMessaging=GoogleCloudMessaging.getInstance(context);
            String messageType=googleCloudMessaging.getMessageType(intent);

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
                sendNotification("MESSAGE TYPE ERROR", true);
                Log.d(TAG, "MESSAGE TYPE ERROR");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)){
                Log.d(TAG, "Delete on  Server");
                sendNotification("MESSAGE TYPE DELETE", true);
            } else {
                sendNotification("MESSAGE TYPE OK", true);
                Log.d(TAG, "Message is OK "+intent.getExtras().toString());
            }
        } finally {
            mWakeLock.release();
        }
        setResultCode(Activity.RESULT_OK);
    }

    private void sendNotification(String text, boolean launchApp) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder mBuilder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(text);


        if (launchApp) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
        }

        mNotificationManager.notify(1, mBuilder.getNotification());
    }


}
