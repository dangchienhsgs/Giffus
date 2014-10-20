package com.dangchienhsgs.giffus.account;

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
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setRegistrationsID(String registrationsID) {
        this.registrationsID = registrationsID;
    }

    public void setIsWannaReceive(String isWannaReceive) {
        this.isWannaReceive = isWannaReceive;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGenre() {
        return genre;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getJob() {
        return job;
    }

    public String getRegistrationsID() {
        return registrationsID;
    }

    public String getIsWannaReceive() {
        return isWannaReceive;
    }
}
