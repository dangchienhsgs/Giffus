package com.dangchienhsgs.giffus.postcard;

/**
 * Created by dangchienbn on 20/10/2014.
 */
public class Inner {
    private String background;
    private String senderName;
    private String avatarID;

    private String mainMess;
    private String endMess;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setAvatarID(String avatarID) {
        this.avatarID = avatarID;
    }

    public void setMainMess(String mainMess) {
        this.mainMess = mainMess;
    }

    public void setEndMess(String endMess) {
        this.endMess = endMess;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getAvatarID() {
        return avatarID;
    }

    public String getMainMess() {
        return mainMess;
    }

    public String getEndMess() {
        return endMess;
    }
}
