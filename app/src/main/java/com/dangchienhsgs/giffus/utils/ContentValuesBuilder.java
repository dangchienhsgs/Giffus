package com.dangchienhsgs.giffus.utils;

import android.content.ContentValues;

import com.dangchienhsgs.giffus.account.Human;
import com.dangchienhsgs.giffus.provider.FriendContract;


public class ContentValuesBuilder {
    public static ContentValues friendBuilder(Human human){
        if (human!=null){
            ContentValues contentValues=new ContentValues();
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

}
