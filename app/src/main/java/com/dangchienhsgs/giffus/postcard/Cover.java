package com.dangchienhsgs.giffus.postcard;


public class Cover {
    private String image_id;
    private String largeText;
    private String smallText;

    private String type;

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public void setLargeText(String largeText) {
        this.largeText = largeText;
    }

    public void setSmallText(String smallText) {
        this.smallText = smallText;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage_id() {
        return image_id;
    }

    public String getLargeText() {
        return largeText;
    }

    public String getSmallText() {
        return smallText;
    }

    public String getType() {
        return type;
    }
}
