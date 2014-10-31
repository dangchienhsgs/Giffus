package com.dangchienhsgs.giffus.postcard;

import com.dangchienhsgs.giffus.media.Song;

import java.util.List;


public class Inner {
    public static final String NAME = "inner";

    public static final String JSON_NAME = "json_inner";
    public static final String BACKGROUND_ID = "background";
    public static final String AVATAR_ID = "avatar_id";

    public static final String SMALL_TEXT = "inner_small_text";
    public static final String COLOR_SMALL_TEXT = "inner_color_small_text";
    public static final String SIZE_SMALL_TEXT = "inner_size_small_text";
    public static final String SMALL_TEXT_FONT = "inner_small_text_font";

    public static final String LARGE_TEXT = "inner_large_text";
    public static final String COLOR_LARGE_TEXT = "inner_color_large_text";
    public static final String SIZE_LARGE_TEXT = "inner_size_large_text";
    public static final String LARGE_TEXT_FONT = "inner_large_text_font";

    public static final String SONG_URLS = "song_urls";
    public static final String SONG_LYRICS = "song_lyrics";
    public static final String SONG_TITLE = "song_titles";

    private int backgroundID;
    private int avatarID;

    private String textLarge;
    private int colorTextLarge;
    private float sizeTextLarge;
    private String fontTextLarge;


    private String textSmall;
    private int colorTextSmall;
    private float sizeTextSmall;
    private String fontTextSmall;
    private List<Song> listSongs;

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
}
