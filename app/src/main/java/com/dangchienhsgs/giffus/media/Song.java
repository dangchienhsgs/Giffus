package com.dangchienhsgs.giffus.media;

/**
 * Created by dangchienbn on 23/10/2014.
 */
public class Song {
    private String title;
    private String url;
    private String lyrics;

    public Song(String title, String url, String lyrics) {
        this.title = title;
        this.url = url;
        this.lyrics = lyrics;
    }

    public Song(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getLyrics() {

        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
