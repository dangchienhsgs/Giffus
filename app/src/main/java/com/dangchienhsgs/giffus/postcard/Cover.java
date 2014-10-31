package com.dangchienhsgs.giffus.postcard;


import android.content.ContentValues;

public class Cover {
    public static final String JSON_NAME = "json_cover";
    public static final String BACKGROUND_ID = "cover_image_id";

    public static final String SMALL_TEXT = "cover_small_text";
    public static final String COLOR_SMALL_TEXT = "cover_color_small_text";
    public static final String SIZE_SMALL_TEXT = "cover_size_small_text";
    public static final String SMALL_TEXT_FONT = "cover_small_text_font";

    public static final String LARGE_TEXT = "cover_large_text";
    public static final String COLOR_LARGE_TEXT = "cover_color_large_text";
    public static final String SIZE_LARGE_TEXT = "cover_size_large_text";
    public static final String LARGE_TEXT_FONT = "cover_large_text_font";
    public static final String TYPE = "cover_type";
    private String fontTextLarge;
    private String fontTextSmall;
    private int backgroundID;
    private String textLarge;
    private int colorLargeText;
    private float sizeLargeText;
    private float sizeSmallText;
    private String textSmall;
    private int colorSmallText;
    private String type;

    public int getBackgroundID() {
        return backgroundID;
    }

    public void setBackgroundID(int backgroundID) {
        this.backgroundID = backgroundID;
    }

    public float getSizeLargeText() {

        return sizeLargeText;
    }

    public void setSizeLargeText(float sizeLargeText) {
        this.sizeLargeText = sizeLargeText;
    }

    public float getSizeSmallText() {
        return sizeSmallText;
    }

    public void setSizeSmallText(float sizeSmallText) {
        this.sizeSmallText = sizeSmallText;
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

    public int getColorSmallText() {

        return colorSmallText;
    }

    public void setColorSmallText(int colorSmallText) {
        this.colorSmallText = colorSmallText;
    }

    public int getColorLargeText() {

        return colorLargeText;
    }

    public void setColorLargeText(int colorLargeText) {
        this.colorLargeText = colorLargeText;
    }

    public String getTextLarge() {
        return textLarge;
    }

    public void setTextLarge(String textLarge) {
        this.textLarge = textLarge;
    }

    public String getTextSmall() {
        return textSmall;
    }

    public void setTextSmall(String textSmall) {
        this.textSmall = textSmall;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
