package com.dangchienhsgs.giffus.postcard;

import android.content.ContentValues;

import com.dangchienhsgs.giffus.human.Human;
import com.dangchienhsgs.giffus.map.GiftLocation;

import java.util.List;

public class Postcard {
    private static final String COMMA_SEP = ",";
    private Cover cover;
    private Inner inner;

    private GiftLocation location;
    private String senderID;
    private List<String> receiverID;

    private int year;
    private int month;
    private int day;

    private int sentDay;
    private int sentMonth;
    private int sentYear;

    private int sentHour;
    private int sentMinute;

    private boolean secretSender;
    private String senderFullName;


    private int hour;
    private int minute;

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public Inner getInner() {
        return inner;
    }

    public void setInner(Inner inner) {
        this.inner = inner;
    }

    public String getCOMMA_SEP() {
        return COMMA_SEP;
    }

    public GiftLocation getLocation() {
        return location;
    }

    public void setLocation(GiftLocation location) {
        this.location = location;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public List<String> getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(List<String> receiverID) {
        this.receiverID = receiverID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isSecretSender() {
        return secretSender;
    }

    public void setSecretSender(boolean secretSender) {
        this.secretSender = secretSender;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public int getSentDay() {
        return sentDay;
    }

    public void setSentDay(int sentDay) {
        this.sentDay = sentDay;
    }

    public int getSentMonth() {
        return sentMonth;
    }

    public void setSentMonth(int sentMonth) {
        this.sentMonth = sentMonth;
    }

    public int getSentYear() {
        return sentYear;
    }

    public void setSentYear(int sentYear) {
        this.sentYear = sentYear;
    }

    public int getSentHour() {
        return sentHour;
    }

    public void setSentHour(int sentHour) {
        this.sentHour = sentHour;
    }

    public int getSentMinute() {
        return sentMinute;
    }

    public void setSentMinute(int sentMinute) {
        this.sentMinute = sentMinute;
    }
}
