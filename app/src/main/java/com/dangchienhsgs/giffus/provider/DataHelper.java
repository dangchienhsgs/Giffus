package com.dangchienhsgs.giffus.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DataHelper {
    private static final String TAG="Data Helper";
    private ContentResolver contentResolver;

    public DataHelper(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public boolean updateAllFriendsData(String friends){
        try{
            JSONArray jsonArray=new JSONArray(friends);
            int length=jsonArray.length();
            for (int i=0; i<length; i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                ContentValues contentValues=new ContentValues();
                contentValues.put(FriendContract.Entry.USERNAME, jsonObject.getString(FriendContract.Entry.USERNAME));
                contentValues.put(FriendContract.Entry.USER_ID, jsonObject.getString(FriendContract.Entry.USER_ID));
                contentValues.put(FriendContract.Entry.FULL_NAME, jsonObject.getString(FriendContract.Entry.FULL_NAME));
                contentValues.put(FriendContract.Entry.GENRE, jsonObject.getString(FriendContract.Entry.GENRE));
                contentValues.put(FriendContract.Entry.AVATAR_ID, jsonObject.getString(FriendContract.Entry.AVATAR_ID));
                contentValues.put(FriendContract.Entry.EMAIL, jsonObject.getString(FriendContract.Entry.EMAIL));
                contentValues.put(FriendContract.Entry.MOBILE_PHONE, jsonObject.getString(FriendContract.Entry.MOBILE_PHONE));
                contentValues.put(FriendContract.Entry.BIRTHDAY, jsonObject.getString(FriendContract.Entry.BIRTHDAY));
                contentResolver.insert(FriendContract.FRIEND_CONTENT_URI, contentValues);
            }
            return true;
        } catch (JSONException e){
            Log.d(TAG, "JSONArray Parse is fail");
            return false;
        }
    }
}
