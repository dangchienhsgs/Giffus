package com.dangchienhsgs.giffus.postcard;

import com.dangchienhsgs.giffus.map.Song;

import java.util.List;


public class Inner {

    public static final String AVATAR_ID = "avatar_id";

    private int backgroundID;
    private int avatarID;

    private String textLarge;
    private int colorTextLarge;
    private float sizeTextLarge;
    private String fontTextLarge;
    private int backgroundTextLarge;


    private String textSmall;
    private int colorTextSmall;
    private float sizeTextSmall;
    private String fontTextSmall;
    private int backgroundTextSmall;


    private List<Song> listSongs;

    public Inner() {
        backgroundTextLarge = -1;
        backgroundTextSmall = 0;
    }

    public String getFontTextLarge() {
        return fontTextLarge;
    }

    public void setFontTextLarge(String fontTextLarge) {
        this.fontTextLarge = fontTextLarge;
    }

    public String getFontTextSmall() {
        return fontTextSmall;
    }

    public void setFontTextSmall(String fontTextSmall) {
        this.fontTextSmall = fontTextSmall;
    }

    public int getBackgroundID() {
        return backgroundID;
    }

    public void setBackgroundID(int backgroundID) {
        this.backgroundID = backgroundID;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public String getTextLarge() {
        return textLarge;
    }

    public void setTextLarge(String textLarge) {
        this.textLarge = textLarge;
    }

    public int getColorTextLarge() {
        return colorTextLarge;
    }

    public void setColorTextLarge(int colorTextLarge) {
        this.colorTextLarge = colorTextLarge;
    }

    public float getSizeTextLarge() {
        return sizeTextLarge;
    }

    public void setSizeTextLarge(float sizeTextLarge) {
        this.sizeTextLarge = sizeTextLarge;
    }

    public String getTextSmall() {
        return textSmall;
    }

    public void setTextSmall(String textSmall) {
        this.textSmall = textSmall;
    }

    public int getColorTextSmall() {
        return colorTextSmall;
    }

    public void setColorTextSmall(int colorTextSmall) {
        this.colorTextSmall = colorTextSmall;
    }

    public float getSizeTextSmall() {
        return sizeTextSmall;
    }

    public void setSizeTextSmall(float sizeTextSmall) {
        this.sizeTextSmall = sizeTextSmall;
    }

    public List<Song> getListSongs() {
        return listSongs;
    }

    public void setListSongs(List<Song> listSongs) {

        this.listSongs = listSongs;
    }

    public int getBackgroundTextLarge() {
        return backgroundTextLarge;
    }

    public void setBackgroundTextLarge(int backgroundTextLarge) {
        this.backgroundTextLarge = backgroundTextLarge;
    }

    public int getBackgroundTextSmall() {
        return backgroundTextSmall;
    }

    public void setBackgroundTextSmall(int backgroundTextSmall) {
        this.backgroundTextSmall = backgroundTextSmall;
    }
}
