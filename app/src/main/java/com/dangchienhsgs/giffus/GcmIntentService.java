package com.dangchienhsgs.giffus;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dangchienhsgs.giffus.friend.FriendFragment;
import com.dangchienhsgs.giffus.notification.NotifyActivity;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.friend.Human;
import com.dangchienhsgs.giffus.postcard.Postcard;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.NotificationContract;
import com.dangchienhsgs.giffus.provider.PostcardContract;
import com.dangchienhsgs.giffus.server.ServerUtilities;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.utils.ContentValuesBuilder;
import com.dangchienhsgs.giffus.utils.UserHandler;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;
    private String TAG = "GCMIntentService";
    private NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    /**
     * Send notification and return to notification activity
     *
     * @param context
     * @param text
     * @param launchApp
     */
    public static void sendNotification(Context context, String text, boolean launchApp, int typeNoti) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder mBuilder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.present)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(text);


        if (launchApp) {
            Intent intent;
            PendingIntent pi;
            switch (typeNoti) {
                case NotificationContract.TYPE_FRIEND_REQUEST:
                    intent = new Intent(context, NotifyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    break;
                case NotificationContract.TYPE_RECEIVE_POST_CARD:
                    intent = new Intent(context, NotifyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    break;
                case NotificationContract.TYPE_RECEIVE_ACCEPT_FRIEND:
                    intent = new Intent(context, NotifyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

        } else if (action.equals(Common.ACTION_ACCEPTED_FRIENDSHIP)) {
            // Your request was been accepted
            String acceptedID = intent.getStringExtra(Common.MESSAGE_ACCEPTED_USER_ID);
            onReceivedAcceptFriend(acceptedID);
        } else if (action.equals(Common.ACTION_SEND_POSTCARD)) {

            String senderID = intent.getStringExtra(PostcardContract.Entry.SENDER_ID);
            String receiverID = intent.getStringExtra(PostcardContract.Entry.RECEIVER_ID);
            String jsonPostcard = intent.getStringExtra(PostcardContract.Entry.JSON_POSTCARD);

            Log.d(TAG, jsonPostcard);
            onReceivePostcard(jsonPostcard, senderID, receiverID);
        } else if (action.equals(Common.ACTION_DECLINE_FRIENDSHIP)) {
            String declinedID = intent.getStringExtra(Common.MESSAGE_DECLINED_USER_ID);
            String requireID = intent.getStringExtra(Common.MESSAGE_REQUIRE_USER_ID);

            if (requireID.equals(PreferencesHandler.getValueFromPreferences(Common.USER_ID, getApplicationContext()))) {
                onReceiveDecline(declinedID);
            }
        } else if (action.equals(Common.ACTION_REMOVE_FRIENDSHIP)) {
            String requestID = intent.getStringExtra(Common.MESSAGE_REQUEST_USER_ID);
            String removedID = intent.getStringExtra(Common.MESSAGE_REMOVED_USER_ID);

            if (removedID.equals(PreferencesHandler.getValueFromPreferences(Common.USER_ID, getApplicationContext()))) {
                removeFriend(requestID);
            }
        }
    }

    public void removeFriend(String requestID) {
        Log.d(TAG, "On Decline Accept Friend");
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(
                FriendContract.URI,
                FriendContract.Entry.USER_ID + "=" + "'" + requestID + "'",
                null
        );
    }

    public void onReceiveDecline(String declinedID) {
        Log.d(TAG, "On Decline Accept Friend");
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(
                FriendContract.URI,
                FriendContract.Entry.USER_ID + "=" + "'" + declinedID + "'",
                null
        );
    }

    public void onReceivePostcard(String jsonPostcard, String senderID, String receiverID) {
        jsonPostcard = jsonPostcard.replace("\\\"", "\"");
        Log.d(TAG, jsonPostcard);
        Postcard postcard = new Gson().fromJson(jsonPostcard, Postcard.class);
        ContentValues contentValues = new ContentValues();
        contentValues.put(PostcardContract.Entry.JSON_POSTCARD, jsonPostcard);
        contentValues.put(PostcardContract.Entry.SENDER_ID, senderID);
        contentValues.put(PostcardContract.Entry.RECEIVER_ID, receiverID);
        getContentResolver().insert(PostcardContract.URI, contentValues);

        Cursor cursor = getContentResolver().query(
                FriendContract.URI,
                null,
                FriendContract.Entry.USER_ID + "=" + "'" + senderID + "'",
                null,
                null
        );

        String senderFullName = "";
        int senderAvatarID;

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            senderAvatarID = cursor.getInt(cursor.getColumnIndex(FriendContract.Entry.AVATAR_ID));
            senderFullName = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.FULL_NAME));

        } else {
            // get your friend full name from internet
            Map<String, String> params = new HashMap<String, String>();
            params.put(Common.USER_ID, senderID);
            params.put(Common.ACTION, Common.ACTION_GET_USER_INFO_BY_ID);
            Human human;
            try {
                human = new Human(ServerUtilities.postToServer(ServerUtilities.SERVER_NAME, params));
                senderFullName = human.getFullName();
                senderAvatarID = Integer.parseInt(human.getAvatarID());
            } catch (JSONException e) {
                Log.d(TAG, "JSON Exception");
                senderAvatarID = Common.HUMAN_ICON.length - 1;
            }

        }

        if (postcard.isSecretSender()) senderAvatarID = Common.HUMAN_ICON.length - 1;

        String title;

        if (senderFullName.isEmpty() || postcard.isSecretSender()) {
            title = "A secret friend send you a postcard";
        } else {
            title = "Your friend " + senderFullName + " send you a postcard";
        }


        // add notification to data provider
        ContentValues notificationValues = new ContentValues();
        Calendar calendar = Calendar.getInstance();

        notificationValues.put(NotificationContract.Entry.FRIEND_ID, senderID);
        notificationValues.put(NotificationContract.Entry.ENABLE, true);
        notificationValues.put(NotificationContract.Entry.TYPE, NotificationContract.TYPE_RECEIVE_POST_CARD);
        notificationValues.put(NotificationContract.Entry.MESSAGE, title);
        notificationValues.put(NotificationContract.Entry.YEAR, calendar.get(Calendar.YEAR));
        notificationValues.put(NotificationContract.Entry.MONTH, calendar.get(Calendar.MONTH));
        notificationValues.put(NotificationContract.Entry.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        notificationValues.put(NotificationContract.Entry.HOUR, calendar.get(Calendar.HOUR_OF_DAY));
        notificationValues.put(NotificationContract.Entry.MINUTE, calendar.get(Calendar.MINUTE));
        notificationValues.put(NotificationContract.Entry.SECOND, calendar.get(Calendar.SECOND));
        notificationValues.put(NotificationContract.Entry.AVATAR_ID, senderAvatarID);

        getContentResolver().insert(NotificationContract.URI, notificationValues);

        sendNotification(
                this,
                title,
                true,
                NotificationContract.TYPE_RECEIVE_POST_CARD
        );
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

            Calendar calendar = Calendar.getInstance();

            notifyContentValues.put(NotificationContract.Entry.MONTH, calendar.get(calendar.MONTH));
            notifyContentValues.put(NotificationContract.Entry.DAY, calendar.get(calendar.DAY_OF_MONTH));
            notifyContentValues.put(NotificationContract.Entry.HOUR, calendar.get(calendar.HOUR_OF_DAY));
            notifyContentValues.put(NotificationContract.Entry.MINUTE, calendar.get(calendar.MINUTE));
            notifyContentValues.put(NotificationContract.Entry.SECOND, calendar.get(calendar.SECOND));
            notifyContentValues.put(NotificationContract.Entry.YEAR, calendar.get(calendar.YEAR));

            notifyContentValues.put(NotificationContract.Entry.ENABLE, 1);
            notifyContentValues.put(NotificationContract.Entry.FRIEND_ID, friend.getUserID());
            notifyContentValues.put(NotificationContract.Entry.MESSAGE, friend.getFullName() + " want to be your friend");
            notifyContentValues.put(NotificationContract.Entry.TYPE, NotificationContract.TYPE_FRIEND_REQUEST);
            notifyContentValues.put(NotificationContract.Entry.AVATAR_ID, friend.getAvatarID());

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
                FriendContract.Entry.USER_ID + "=" + "'" + acceptID + "'",
                null
        );

        sendNotification(
                getApplicationContext(),
                UserHandler.getFriendInfo(getApplicationContext(), FriendContract.Entry.USER_ID, acceptID, FriendContract.Entry.FULL_NAME)
                        + " has been accept your friend request",
                true,
                NotificationContract.TYPE_RECEIVE_ACCEPT_FRIEND
        );
    }
}
