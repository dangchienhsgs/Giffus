package com.dangchienhsgs.giffus;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dangchienhsgs.giffus.human.Human;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.NotificationContract;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.ContentValuesBuilder;

import org.json.JSONException;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;
    private String TAG = "GCMIntentService";
    private NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    /**
     * Send notification and return to notification acitvity
     *
     * @param context
     * @param text
     * @param launchApp
     */
    public static void sendNotification(Context context, String text, boolean launchApp, int typeNoti) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder mBuilder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(text);


        if (launchApp) {
            switch (typeNoti) {
                case NotificationContract.TYPE_FRIEND_REQUEST:
                    Intent intent = new Intent(context, FriendRequestsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    break;
            }
        }

        mNotificationManager.notify(1, mBuilder.getNotification());
    }

    @Override

    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, intent.getExtras().toString());
        String action = intent.getStringExtra(Common.MESSAGE_ACTION);
        if (action.equals(Common.ACTION_MESSAGE_INVITED)) {
            // There are a people who request to be a friend;
            String requireID = intent.getStringExtra(Common.MESSAGE_REQUIRE_USER_ID);
            onReceiveRequestFriend(requireID);

        } else if (action.equals(Common.ACTION_MESSAGE_ACCEPTED)) {
            // Your request was been accepted
            String acceptedID = intent.getStringExtra(Common.MESSAGE_ACCEPTED_USER_ID);
            onReceivedAcceptFriend(acceptedID);
        }
    }

    /**
     * When the user with user_id=requireID want to be your friend
     * This routine handle it;
     *
     * @param requireID
     */
    public void onReceiveRequestFriend(String requireID) {
        String jsonInfo = ServerUtilities.getUserInfoById(requireID);
        Log.d(TAG, "Info of people sent request: " + jsonInfo);

        // Convert from JSON to Human
        try {
            Human friend = new Human(jsonInfo);
            sendNotification(
                    getApplicationContext(),
                    friend.getFullName() + " want to be your friend",
                    true,
                    NotificationContract.TYPE_FRIEND_REQUEST
            );

            // Put new notification
            ContentValues notifyContentValues = new ContentValues();
            ContentResolver contentResolver = getContentResolver();
            notifyContentValues.put(NotificationContract.Entry.ENABLE, 1);
            notifyContentValues.put(NotificationContract.Entry.FRIEND_ID, friend.getUserID());
            notifyContentValues.put(NotificationContract.Entry.MESSAGE, friend.getFullName() + " want to be your friend");
            notifyContentValues.put(NotificationContract.Entry.TYPE, NotificationContract.TYPE_FRIEND_REQUEST);
            contentResolver.insert(NotificationContract.URI, notifyContentValues);

            // Put new friend values
            ContentValues friendContentValues = ContentValuesBuilder.friendBuilder(friend);
            friendContentValues.put(FriendContract.Entry.RELATIONSHIP, FriendContract.WAIT_ACCEPTING);
            contentResolver.insert(FriendContract.URI, friendContentValues);

        } catch (JSONException e) {
            Log.d(TAG, "Friend JSON cannot parsing");
        }
    }

    public void onReceivedAcceptFriend(String acceptID) {
        Log.d(TAG, "On Receive Accept Friend");
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FriendContract.Entry.RELATIONSHIP, FriendContract.ALREADY_FRIEND);
        Log.d(TAG, contentValues.toString());
        contentResolver.update(
                FriendContract.URI,
                contentValues,
                FriendContract.Entry.USER_ID + "=" + acceptID,
                null
        );
    }
}
