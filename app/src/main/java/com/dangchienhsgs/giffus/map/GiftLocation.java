package com.dangchienhsgs.giffus.map;


public class GiftLocation {
    private String googleMapID;

    // Location position
    private double latitude;
    private double longitude;
    private double references;

    // The name of this location
    private String mapTitle;

    private String alternativeTitle;
    // The hint string for the people want to find it
    private String hint;
    // Secret Option
    private boolean isSecret;

    public double getReferences() {

        return references;
    }

    public void setReferences(double references) {
        this.references = references;
    }

    public String getGoogleMapID() {
        return googleMapID;
    }

    public void setGoogleMapID(String googleMapID) {
        this.googleMapID = googleMapID;
    }

    public String getAlternativeTitle() {
        return alternativeTitle;
    }

    public void setAlternativeTitle(String alternativeTitle) {
        this.alternativeTitle = alternativeTitle;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public void setSecret(boolean isSecret) {
        this.isSecret = isSecret;
    }

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

    public String getMapTitle() {
        return mapTitle;
    }

    public void setMapTitle(String mapTitle) {
        this.mapTitle = mapTitle;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
