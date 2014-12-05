package com.dangchienhsgs.giffus.utils;

import android.content.ContentValues;
import android.util.Log;

import com.dangchienhsgs.giffus.friend.Human;
import com.dangchienhsgs.giffus.postcard.Postcard;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.PostcardContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ContentValuesBuilder {
    private static final String TAG = "Content Values Builder";

    public static ContentValues friendBuilder(Human human) {
        if (human != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FriendContract.Entry.AVATAR_ID, human.getAvatarID());
            contentValues.put(FriendContract.Entry.USERNAME, human.getUsername());
            contentValues.put(FriendContract.Entry.USER_ID, human.getUserID());
            contentValues.put(FriendContract.Entry.BIRTHDAY, human.getBirthday());
            contentValues.put(FriendContract.Entry.MOBILE_PHONE, human.getMobilePhone());
            contentValues.put(FriendContract.Entry.FULL_NAME, human.getFullName());
            contentValues.put(FriendContract.Entry.GENRE, human.getGenre());
            contentValues.put(FriendContract.Entry.EMAIL, human.getEmail());
            return contentValues;
        } else {
            return null;
        }
    }

    public static List<ContentValues> giftBuilder(Postcard postcard) {

        List<ContentValues> list = new ArrayList<ContentValues>();

        for (int i = 0; i < postcard.getReceiverID().size(); i++) {
            String jsonPostcard = new Gson().toJson(postcard, Postcard.class);
            ContentValues contentValues = new ContentValues();

            contentValues.put(PostcardContract.Entry.JSON_POSTCARD, jsonPostcard);
            Log.d(TAG, jsonPostcard);
            contentValues.put(PostcardContract.Entry.POSTCARD_ID, UUID.randomUUID().toString());
            contentValues.put(PostcardContract.Entry.SENDER_ID, postcard.getSenderID());
            contentValues.put(PostcardContract.Entry.RECEIVER_ID, postcard.getReceiverID().get(i));

            list.add(contentValues);
        }
        // store postcard

        return list;
    }

}
