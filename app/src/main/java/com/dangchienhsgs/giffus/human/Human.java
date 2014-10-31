package com.dangchienhsgs.giffus.human;

import android.content.ContentValues;

import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dangchienbn on 15/10/2014.
 */
public class Human {

    private String userID;
    private String email;
    private String birthday;
    private String genre;
    private String mobilePhone;
    private String fullName;
    private String username;
    private String job;
    private String registrationsID;
    private String isWannaReceive;
    private String avatarID;

    public Human(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        userID = jsonObject.getString(Common.USER_ID);
        email = jsonObject.getString(Common.EMAIL);
        birthday = jsonObject.getString(Common.BIRTHDAY);
        genre = jsonObject.getString(Common.GENDER);
        mobilePhone = jsonObject.getString(Common.MOBILE_PHONE);
        fullName = jsonObject.getString(Common.FULL_NAME);
        username = jsonObject.getString(Common.USERNAME);
        job = jsonObject.getString(Common.JOB);
        registrationsID = jsonObject.getString(Common.REGISTRATION_ID);
        avatarID = jsonObject.getString(Common.AVATAR_ID);
        isWannaReceive = jsonObject.getString(Common.IS_WANNA_RECEIVE);
    }

    public Human(JSONObject jsonObject) throws JSONException {
        userID = jsonObject.getString(Common.USER_ID);
        email = jsonObject.getString(Common.EMAIL);
        birthday = jsonObject.getString(Common.BIRTHDAY);
        genre = jsonObject.getString(Common.GENDER);
        mobilePhone = jsonObject.getString(Common.MOBILE_PHONE);
        fullName = jsonObject.getString(Common.FULL_NAME);
        username = jsonObject.getString(Common.USERNAME);
        job = jsonObject.getString(Common.JOB);
        registrationsID = jsonObject.getString(Common.REGISTRATION_ID);
        isWannaReceive = jsonObject.getString(Common.IS_WANNA_RECEIVE);
        avatarID = jsonObject.getString(Common.AVATAR_ID);
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FriendContract.Entry.AVATAR_ID, this.getAvatarID());
        contentValues.put(FriendContract.Entry.USERNAME, this.getUsername());
        contentValues.put(FriendContract.Entry.USER_ID, this.getUserID());
        contentValues.put(FriendContract.Entry.BIRTHDAY, this.getBirthday());
        contentValues.put(FriendContract.Entry.MOBILE_PHONE, this.getMobilePhone());
        contentValues.put(FriendContract.Entry.FULL_NAME, this.getFullName());
        contentValues.put(FriendContract.Entry.GENRE, this.getGenre());
        contentValues.put(FriendContract.Entry.EMAIL, this.getEmail());
        return contentValues;
    }

    public String getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(String avatarID) {
        this.avatarID = avatarID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRegistrationsID() {
        return registrationsID;
    }

    public void setRegistrationsID(String registrationsID) {
        this.registrationsID = registrationsID;
    }

    public String getIsWannaReceive() {
        return isWannaReceive;
    }

    public void setIsWannaReceive(String isWannaReceive) {
        this.isWannaReceive = isWannaReceive;
    }
}
