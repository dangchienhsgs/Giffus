package com.dangchienhsgs.giffus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.dangchienhsgs.giffus.client.PreferencesHandler;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dangchienbn on 15/10/2014.
 */
public class UserHandler {
    private final static String TAG = "UserHandler";

    public static boolean savedUserInfoToPreferences(SharedPreferences preferences, String jsonUser) {
        try {
            // Analyze JSON String
            JSONObject jsonObject = new JSONObject(jsonUser);
            String userID = jsonObject.getString(Common.USER_ID);
            Log.d(TAG, "USER ID" + userID);
            String email = jsonObject.getString(Common.EMAIL);
            Log.d(TAG, "EMAIL" + email);
            String birthday = jsonObject.getString(Common.BIRTHDAY);
            Log.d(TAG, "BIRTHDAY " + birthday);
            String genre = jsonObject.getString(Common.GENDER);
            Log.d(TAG, "GENRE " + genre);
            String mobilePhone = jsonObject.getString(Common.MOBILE_PHONE);
            Log.d(TAG, "MOBILE PHONE " + mobilePhone);
            String fullName = jsonObject.getString(Common.FULL_NAME);
            Log.d(TAG, "FULL NAME " + fullName);
            String username = jsonObject.getString(Common.USERNAME);
            Log.d(TAG, "USERNAME " + username);
            String job = jsonObject.getString(Common.JOB);
            Log.d(TAG, "JOB " + job);
            String registrationsID = jsonObject.getString(Common.REGISTRATION_ID);
            Log.d(TAG, "REGISTRATIONS " + registrationsID);
            String isWannaReceive = jsonObject.getString(Common.IS_WANNA_RECEIVE);
            Log.d(TAG, "is Wanna receiver" + isWannaReceive);
            String avatarID = jsonObject.getString(Common.AVATAR_ID);


            // Commit to Preferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Common.USER_ID, userID);
            editor.putString(Common.FULL_NAME, fullName);
            editor.putString(Common.EMAIL, email);
            editor.putString(Common.USERNAME, username);
            editor.putString(Common.REGISTRATION_ID, registrationsID);
            editor.putString(Common.MOBILE_PHONE, mobilePhone);
            editor.putString(Common.BIRTHDAY, birthday);
            editor.putString(Common.JOB, job);
            editor.putString(Common.IS_WANNA_RECEIVE, isWannaReceive);
            editor.putString(Common.GENDER, genre);
            editor.putString(Common.AVATAR_ID, avatarID);
            editor.commit();
            return true;
        } catch (JSONException e) {
            Log.d(TAG, "JSON String is error in parsing");
            return false;
        }
    }

    public static String getUserAttribute(Context context, String attribute) {
        return PreferencesHandler.getValueFromPreferences(attribute, context);
    }

    public static String getFriendInfo(Context context, String selectionAttr, String value, String destinationAttr) {
        Cursor cursor = context.getContentResolver().query(
                FriendContract.URI,
                null,
                selectionAttr + "=" + value,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            return null;
        } else {
            cursor.moveToPosition(0);
            return cursor.getString(cursor.getColumnIndex(destinationAttr));
        }
    }

    public static int checkFriend(Context context, String friendID) {
        Cursor cursor = context.getContentResolver().query(
                FriendContract.URI,
                null,
                FriendContract.Entry.USER_ID + "=" + friendID,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            return FriendContract.NO_RELATION;
        } else {
            cursor.moveToPosition(0);
            return Integer.parseInt(cursor.getString(cursor.getColumnIndex(FriendContract.Entry.RELATIONSHIP)));
        }
    }
}
