package com.dangchienhsgs.giffus;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dangchienhsgs.giffus.account.Human;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "GCMBroadcastReceiver";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();
        try {
            GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(context);
            String messageType = googleCloudMessaging.getMessageType(intent);

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                // Message is error in type
                Log.d(TAG, "Message 's type is error !");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                // Message was deleted in server
                Log.d(TAG, "Message was deleted in server");
            } else {
                // Message send OK
                Log.d(TAG, intent.getExtras().toString());
                String message_action = intent.getStringExtra(Common.MESSAGE_ACTION);
                if (message_action != null) {
                    ComponentName componentName = new ComponentName(context.getPackageName(),
                            GcmIntentService.class.getName());
                    startWakefulService(context, (intent.setComponent(componentName)));
                    setResultCode(Activity.RESULT_OK);
                }
            }
        } finally {
            mWakeLock.release();
        }
        setResultCode(Activity.RESULT_OK);
    }
}
