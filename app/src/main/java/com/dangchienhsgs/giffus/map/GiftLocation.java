package com.dangchienhsgs.giffus.map;


public class GiftLocation {
    // Location position
    private double latitude;
    private double longitude;

    // The name of this location
    private String title;

    // The hint string for the people want to find it
    private String hint;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
