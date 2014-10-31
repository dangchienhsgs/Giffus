package com.dangchienhsgs.giffus.utils;


public class UrlBuilder {
    private String url;
    private String serverLink;

    public UrlBuilder(String serverLink) {
        url = serverLink;
        this.serverLink = serverLink;
    }

    public UrlBuilder append(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            if (url.equals(serverLink)) {
                url = url + "?";
            } else {
                url = url + "&";
            }
            url = url + key + "=" + value;
        }
        return this;
    }

    public String create() {
        return url;
    }
}
