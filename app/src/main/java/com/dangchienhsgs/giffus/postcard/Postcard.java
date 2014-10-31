package com.dangchienhsgs.giffus.postcard;

import android.content.ContentValues;

import com.dangchienhsgs.giffus.human.Human;

public class Postcard {
    private final String COMMA_SEP = ",";
    private Cover cover;
    private Inner inner;
    private Human sender;
    private Human receiver;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        // SAVE COVER
        values.put(Cover.BACKGROUND_ID, cover.getBackgroundID());

        values.put(Cover.COLOR_LARGE_TEXT, cover.getColorLargeText());
        values.put(Cover.COLOR_SMALL_TEXT, cover.getColorSmallText());

        values.put(Cover.LARGE_TEXT, cover.getTextLarge());
        values.put(Cover.SMALL_TEXT, cover.getTextSmall());

        values.put(Cover.LARGE_TEXT_FONT, cover.getFontTextLarge());
        values.put(Cover.SMALL_TEXT_FONT, cover.getFontTextSmall());

        values.put(Cover.SIZE_SMALL_TEXT, cover.getSizeSmallText());
        values.put(Cover.SIZE_LARGE_TEXT, cover.getSizeLargeText());


        // SAVE INNER
        values.put(Inner.AVATAR_ID, inner.getAvatarID());
        values.put(Inner.BACKGROUND_ID, inner.getBackgroundID());

        values.put(Inner.LARGE_TEXT, inner.getTextLarge());
        values.put(Inner.SMALL_TEXT, inner.getTextSmall());

        values.put(Inner.LARGE_TEXT_FONT, inner.getFontTextLarge());
        values.put(Inner.SMALL_TEXT_FONT, inner.getFontTextSmall());

        values.put(Inner.COLOR_LARGE_TEXT, inner.getColorTextLarge());
        values.put(Inner.COLOR_SMALL_TEXT, inner.getColorTextSmall());

        values.put(Inner.SIZE_LARGE_TEXT, inner.getSizeTextLarge());
        values.put(Inner.SIZE_SMALL_TEXT, inner.getSizeTextSmall());

        // Put Songs and Path and Lyric
        String listTitle = "";
        String listUrl = "";
        String listLyric = "";

        for (int i = 0; i < inner.getListSongs().size(); i++) {
            listTitle = inner.getListSongs().get(i).getTitle();
            listUrl = inner.getListSongs().get(i).getUrl();
            listLyric = inner.getListSongs().get(i).getUrl();

            if (i < inner.getListSongs().size() - 1) {
                listTitle = listTitle + COMMA_SEP;
                listUrl = listUrl + COMMA_SEP;
                listLyric = listLyric + COMMA_SEP;
            }
        }

        values.put(Inner.SONG_LYRICS, listLyric);
        values.put(Inner.SONG_TITLE, listTitle);
        values.put(Inner.SONG_URLS, listUrl);


        return values;
    }

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

    public Human getSender() {
        return sender;
    }

    public void setSender(Human sender) {
        this.sender = sender;
    }

    public Human getReceiver() {
        return receiver;
    }

    public void setReceiver(Human receiver) {
        this.receiver = receiver;
    }
}
