package com.dangchienhsgs.giffus;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final int NOTIFICATION_ID=1;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override

    protected void onHandleIntent(Intent intent) {
        Log.d("Service", "OK da nhan duoc");
        Bundle extras=intent.getExtras();
        GoogleCloudMessaging gcm=GoogleCloudMessaging.getInstance(this);
        // Do job here
        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), extras.toString(), Toast.LENGTH_SHORT).show();

    }
    private void sendNotification(String msg){
        // Notify here
    }
}
